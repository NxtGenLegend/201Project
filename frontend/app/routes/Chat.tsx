import '../styles/Chat.css';
import { useEffect } from "react";
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export default function Chat() {

    var stompClient: CompatClient | null = null;
    var messagesArea: HTMLElement | null;

    function setConnected(connected: boolean) {
      const connectButton = document.getElementById('connect');
      if (connectButton) {
          (connectButton as HTMLButtonElement).disabled = connected;
      }
      const disconnectButton = document.getElementById('disconnect');
      if (disconnectButton) {
          (disconnectButton as HTMLButtonElement).disabled = !connected;
      }
      if (connected) {
          if (messagesArea) {
              messagesArea.textContent += "* Connected to WebSocket\n";
          }
      } else {
          if (messagesArea) {
              messagesArea.textContent += "* Disconnected from WebSocket\n";
          }
      }
  }

  function connect() {
      console.log("Connecting to WebSocket...");
      var socket = new SockJS('http://localhost:8080/ws');
      stompClient = Stomp.over(() => socket);
      stompClient.connect({}, function (frame: string) {
          setConnected(true);
          console.log('Connected: ' + frame);
          
          if (stompClient) {
              stompClient.subscribe('/topic/public', function (message) {
                  showMessage(JSON.parse(message.body));
              });
          }
          
          if (stompClient) {
              stompClient.subscribe('/user/queue/private', function (message) {
                  showMessage(JSON.parse(message.body));
              });
          }
          
          var senderElement = document.getElementById('sender');
          var senderId = senderElement ? (senderElement as HTMLInputElement).value : null;
          if (senderId && stompClient) {
              stompClient.send("/app/chat.addUser",
                  {},
                  JSON.stringify({
                      sender: senderId,
                      type: 'JOIN'
                  })
              );
          }
      }, function(error: string) {
          console.log("Error: " + error);
          if (messagesArea) {
              messagesArea.textContent += "* Error connecting: " + error + "\n";
          }
      });
  }

  function disconnect() {
      if (stompClient !== null) {
          stompClient.disconnect();
      }
      setConnected(false);
      console.log("Disconnected");
  }

  function sendPrivateMessage() {
      var senderElement = document.getElementById('sender');
      var sender = senderElement ? (senderElement as HTMLInputElement).value : null;
      var recipientElement = document.getElementById('recipient');
      var recipient = recipientElement ? (recipientElement as HTMLInputElement).value : null;
      var messageElement = document.getElementById('message');
      var message = messageElement ? (messageElement as HTMLInputElement).value : null;
      
      if (stompClient && sender && recipient && message) {
          var chatMessage = {
              sender: sender,
              recipient: recipient,
              content: message,
              type: 'CHAT'
          };
          stompClient.send("/app/chat.sendPrivateMessage", {}, JSON.stringify(chatMessage));
          const messageElement = document.getElementById('message') as HTMLInputElement | null;
          if (messageElement) {
              messageElement.value = '';
          }
          if (messagesArea) {
              messagesArea.textContent += `* Sent private message to ${recipient}: ${message}\n`;
          }
      } else {
          alert("Please fill all fields and ensure you're connected");
      }
  }

  function sendGroupMessage() {
      var senderElement = document.getElementById('sender');
      var sender = senderElement ? (senderElement as HTMLInputElement).value : null;
      var recipientElement = document.getElementById('recipient');
      var groupId = recipientElement ? (recipientElement as HTMLInputElement).value : null;
      var messageElement = document.getElementById('message') as HTMLInputElement | null;
      var message = messageElement ? messageElement.value : null;
      
      if (stompClient && sender && groupId && message) {
          stompClient.subscribe(`/topic/group/${groupId}`, function (message) {
              showMessage(JSON.parse(message.body));
          });
          
          var chatMessage = {
              sender: sender,
              content: message,
              type: 'CHAT'
          };
          stompClient.send(`/app/chat.sendGroupMessage/${groupId}`, {}, JSON.stringify(chatMessage));
          const messageInput = document.getElementById('message') as HTMLInputElement | null;
          if (messageInput) {
              messageInput.value = '';
          }
          if (messagesArea) {
              messagesArea.textContent += `* Sent group message to ${groupId}: ${message}\n`;
          }
      } else {
          alert("Please fill all fields and ensure you're connected");
      }
  }

  function showMessage(message: { type: string; sender: any; content: any; recipient: any; }) {
      var messageType = message.type === 'JOIN' ? 'joined' : 
                        message.type === 'LEAVE' ? 'left' : 
                        'message';
      
      var displayStr = '';
      if (messageType === 'message') {
          displayStr = `${message.sender}: ${message.content}`;
          if (message.recipient) {
              displayStr += ` (to ${message.recipient})`;
          }
      } else {
          displayStr = `${message.sender} ${messageType} the chat`;
      }
      
      if (messagesArea) {
          messagesArea.textContent += displayStr + '\n';
      }
  }

  useEffect(() => {
    console.log(0);
    messagesArea = document.getElementById('messages');

    const connectButton = document.getElementById('connect');
    if (connectButton) {

        connectButton.addEventListener('click', connect);
    }
    const disconnectButton = document.getElementById('disconnect');
    if (disconnectButton) {
        disconnectButton.addEventListener('click', disconnect);
    }
    const sendPrivateButton = document.getElementById('send-private');
    if (sendPrivateButton) {
        sendPrivateButton.addEventListener('click', sendPrivateMessage);
    }
    const sendGroupButton = document.getElementById('send-group');
    if (sendGroupButton) {
        sendGroupButton.addEventListener('click', sendGroupMessage);
    }
  }, []);

  return (
    <div id="main-content" className="container">
        <div className="row">
            <div className="col-md-6">
                <form className="form-inline">
                    <div className="form-group">
                        <label htmlFor="connect">WebSocket connection:</label>
                        <button id="connect" className="btn btn-default" type="button">Connect</button>
                        <button id="disconnect" className="btn btn-default" type="button" disabled>Disconnect</button>
                    </div>
                </form>
            </div>
            <div className="col-md-6">
                <form className="form-inline">
                    <div className="form-group">
                        <label htmlFor="sender">Sender ID:</label>
                        <input type="text" id="sender" className="form-control" placeholder="Your ID"/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="recipient">Recipient ID:</label>
                        <input type="text" id="recipient" className="form-control" placeholder="Recipient ID or Group ID"/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="message">Message:</label>
                        <input type="text" id="message" className="form-control" placeholder="Your message here..."/>
                    </div>
                    <button id="send-private" className="btn btn-default" type="button">Send Private</button>
                    <button id="send-group" className="btn btn-default" type="button">Send Group</button>
                </form>
            </div>
        </div>
        <div className="row">
            <div className="col-md-12">
                <h3>Messages:</h3>
                <pre id="messages"></pre>
            </div>
        </div>
    </div>
    
  );
};