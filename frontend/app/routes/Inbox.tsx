import React, { Suspense, useState, useEffect } from "react";
import "../styles/Inbox.css";
import axios from "axios";

const Header = React.lazy(() => import("../components/Header/Header"));

type Message = {
  content: string;
  groupName: string;
  invitationId: number;
  invitationTime: string;
  messageId: number;
  messageTime: string;
  sender: string;
  userId: number;
};

export default function Inbox() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [selectedMessage, setSelectedMessage] = useState<Message | null>(null);

  const handleSelectMessage = (message: any) => {
    setSelectedMessage(message);
  };

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        let username = localStorage.getItem('username');
        const response = await axios.get(`http://localhost:8080/api/inbox/by-username/${username}`);
        const data = response.data;
          setMessages(data);
        console.log(data);
        
      } catch (err) {
        if (axios.isAxiosError(err) && err.response?.data === "Inbox is empty.") {
          return;          
        } 
        console.error('error:', err);
      }
    };
    fetchMessages();
  }, []);

  const joinGroup = async (invitationId: number, userId: number) => {
    try {
      const response = await axios.post(`http://localhost:8080/api/invitations/${invitationId}/accept?userId=${userId}`);
      const data = response.data;
      console.log(data);   
    } catch (err) {
      console.error('error:', err);
    }
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="inbox-container">
        <div className="inbox-menu">
          <h2>Messages</h2>
          <ul>
            {messages.map((message, index) => (
              <li
                key={index}
                className="message-item"
                onClick={() => handleSelectMessage(message)}
              >
                { message.groupName ? <div className="message-subject">Invitation to join {message.groupName} from {message.sender}</div> : <></> }
                { message.content ? <div className="message-subject">Message from {message.sender}</div> : <></> }
                { message.groupName ? <div className="message-timestamp">{message.invitationTime}</div> : <></> }
                { message.content ? <div className="message-timestamp">{message.messageTime}</div> : <></> }
              </li>
            ))}
          </ul>
        </div>
        <div className="inbox-content">
          {selectedMessage ? (
            <div className="message-details">
              { 
                selectedMessage.groupName ? 
                <>
                  <p><strong>From:</strong> {selectedMessage.sender}</p>
                  <p><strong>Timestamp:</strong> {selectedMessage.invitationTime}</p>
                  <p>{selectedMessage.sender} is inviting you to join the study group {selectedMessage.groupName}.</p>
                  <button onClick={() => joinGroup(selectedMessage.invitationId, selectedMessage.userId)} className="inbox-accept-button">Accept</button>
                </>
                : <></> }
              { 
                selectedMessage.content ? 
                <>
                  <p><strong>From:</strong> {selectedMessage.sender}</p>
                  <p><strong>Timestamp:</strong> {selectedMessage.messageTime}</p>
                  <p>{selectedMessage.content}</p>
                </>
                : <></> }
            </div>
          ) : (
            <div className="no-message-selected">No message is selected, or you have no messages.</div>
          )}
        </div>
      </div>
    </Suspense>
  );
}