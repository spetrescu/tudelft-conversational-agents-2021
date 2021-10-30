# ACCESS ALL THE CODEBASE BY DOWNLOADING GOOGLE DRIVE ARCHIVE

from flask import Flask, request, Response, jsonify
from flask import render_template
from flask_cors import CORS
import flask
import json
from collections import defaultdict
import random

import sys
sys.path.append('../../')

import logging
import click

log = logging.getLogger('werkzeug')
log.setLevel(logging.ERROR)

def secho(text, file=None, nl=None, err=None, color=None, **styles):
    pass

def echo(text, file=None, nl=None, err=None, color=None, **styles):
    pass

click.echo = echo
click.secho = secho

import os
os.environ['CUDA_VISIBLE_DEVICES'] = '0'

import zmq

port = "5565"
context = zmq.Context()
socket = context.socket(zmq.PUB)
socket.bind("tcp://*:%s" % port)
zmq_topic = "Python Furhat user"
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# publishes a message through the zmq server on the specified topic
def publish_msg(topic, message_data):
    print("%s %s" % (topic, str(message_data)))
    socket.send_string("%s %s" % (topic, str(message_data)))

app = Flask(__name__)
CORS(app)

from queue import Queue
from threading import Thread

rgi_queue = Queue(maxsize=0)
rgo_queue = Queue(maxsize=0)

def parse(sampled_results):
    print('----------------------')
    print(sampled_results)
    sys_res = sampled_results[0].split('system : ')[-1]
    if "_" in sys_res and "name" in sampled_results[0]:
        print(1, sys_res)
        sys_res = sampled_results[0].split('system : ')[0]
        print(2, sys_res)
        sys_res = sys_res.replace(' ','')
        print(3, sys_res)
        sys_res = sys_res.split('=')[1]
        print(4, sys_res)
    send_model_response_to_furhat(sys_res)
    candidates = []
    for system_response in sampled_results:
        system_response = system_response.split('system :')[-1]
        system_response = ' '.join(word_tokenize(system_response))
        system_response = system_response.replace('[ ','[').replace(' ]',']')
        candidates.append(system_response)

    candidates_bs = []
    for system_response in sampled_results:
        system_response = system_response.strip()
        system_response = system_response.split('system :')[0]
        system_response = ' '.join(system_response.split()[:])
        svs = system_response.split(' ; ')
        bs_state = {}
        for sv in svs:
            if '=' in sv:
                s,v = sv.split('=')
                s = s.strip()
                v = v.strip()
                bs_state[s] = v
        candidates_bs.append(copy.copy(bs_state))

    candidates_w_idx = [(idx, v) for idx,v in enumerate(candidates)]
    candidates = sorted(candidates_w_idx, key=functools.cmp_to_key(compare))

    idx, response = candidates[-1]
    states = candidates_bs[idx]
    return states,response

def compare(key1, key2):
    key1 = key1[1]
    key2 = key2[1]
    if key1.count('[') > key2.count('['):
        return 1
    elif key1.count('[') == key2.count('['):
        return 1 if len(key1.split()) > len(key2.split()) else -1
    else:
        return -1

def predictor(context):
    #print("context is ", context)
    context_formated = []
    for idx, i in enumerate(context):
        if idx % 2 == 0:
            context_formated.append(f'user : {i}')
        else:
            context_formated.append(f'system : {i}')

    #print("context_formated", context_formated, "\n")
    print(context_formated, "\n")
    sampled_results = sample(context_formated[-1:])
    belief_states, response = parse(sampled_results)

    return response, belief_states

def send_model_response_to_furhat(sys_message):
    print("Sending model response to Furhat serverNluNlg...")
    print("Trying to send: ", sys_message)
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect("tcp://localhost:5897")
    socket.send_string(sys_message)
    print("Sent model response to Furhat serverNluNlg...")

global_counter = 0
@app.route('/generate', methods=['GET','POST'])
def generate_queue():
    global global_counter, rgi_queue, rgo_queue
    try:
        in_request = request.json
        print(in_request)
    except:
        return "invalid input: "
    global_counter += 1
    rgi_queue.put((global_counter, in_request))
    output = rgo_queue.get()
    rgo_queue.task_done()
    return jsonify(output)

def get_usr_input_from_furhat():
    print("\n\nWait for input from Furhat clientNluNlg...")
    port = 5599
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind("tcp://*:%s" % port)
    message = socket.recv_string()
    print("Furhat clientNluNlg sent: ", message, "\n")
    return message

def generate_for_queue(in_queue, out_queue):
    print("Start Flask system...")
    context_messages_list = []
    idx = 0

    while True:
        print("idx: ", idx)
        context_messages_list.append(get_usr_input_from_furhat())
        response, belief_states = predictor(context_messages_list)
        context_messages_list.append(response)
        #print("context_messages_list: \n", context_messages_list + "\n\n")
        idx += 1

    ## Old workflow from the authors of SOLOIST
    memory = []
    while True:
        _, in_request = in_queue.get()
        print("in_request['msg'] = ", in_request['msg'])
        obs = in_request['msg']
        response, belief_states = predictor(obs)
        #if belief_states != {}:
        #    name = belief_states['name']
        #    memory.append(f'reminder call [name]({name})')

        #followup = ''
        #if response.strip() == 'action_set_reminder':
        #    followup = 'Sure thing, added to your reminder list!'
        #if response.strip() == 'action_forget_reminders':
        #    followup = 'Sure thing, remove all your reminders!'
        #    memory = []
        #res = {}
        #res['response'] = response
        #res['memory'] = memory
        #res['followup'] = followup
        #out_queue.put(res)
        #in_queue.task_done()

if __name__ == "__main__":

    from soloist.server import *
    args.model_name_or_path = 'reminderbot_model'
    main()
    worker = Thread(target=generate_for_queue, args=(rgi_queue, rgo_queue,))
    worker.setDaemon(True)
    worker.start()
    app.run(host='127.0.0.1',port=8081)
