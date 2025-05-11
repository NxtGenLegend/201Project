import React, { Suspense, useState, useEffect } from "react";
import "../styles/Inbox.css";
import axios from "axios";

const Header = React.lazy(() => import("../components/Header/Header"));

type Message = {
  id: number;
  timestamp: string;
  subject: string;
  content: string;
  sender: string;
  recipient: string;
  readState: boolean;
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
          // setMessages(data);
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

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="inbox-container">
        <div className="inbox-menu">
          <h2>Messages</h2>
          <ul>
            {messages.map((message) => (
              <li
                key={message.id}
                className={`message-item ${message.readState ? "read" : "unread"}`}
                onClick={() => handleSelectMessage(message)}
              >
                <div className="message-subject">{message.subject}</div>
                <div className="message-timestamp">{message.timestamp}</div>
              </li>
            ))}
          </ul>
        </div>
        <div className="inbox-content">
          {selectedMessage ? (
            <div className="message-details">
              <h3>{selectedMessage.subject}</h3>
              <p><strong>From:</strong> {selectedMessage.sender}</p>
              <p><strong>To:</strong> {selectedMessage.recipient}</p>
              <p><strong>Timestamp:</strong> {selectedMessage.timestamp}</p>
              <p><strong>Content:</strong></p>
              <p>{selectedMessage.content}</p>
            </div>
          ) : (
            <div className="no-message-selected">No message is selected, or you have no messages.</div>
          )}
        </div>
      </div>
    </Suspense>
  );
}