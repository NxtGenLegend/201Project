import React, { Suspense, useState } from "react";
import "../styles/Inbox.css";

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

// Sample messages data
// To be pulled from an API later
const sampleMessages: Message[] = [
  {
    id: 1,
    timestamp: "2025-04-30 10:00 AM",
    subject: "Meeting Reminder",
    content: "Don't forget about the meeting at 3 PM today.",
    sender: "manager@example.com",
    recipient: "you@example.com",
    readState: false,
  },
  {
    id: 2,
    timestamp: "2025-04-29 2:15 PM",
    subject: "Project Update",
    content: "The project deadline has been extended to next Friday.",
    sender: "teamlead@example.com",
    recipient: "you@example.com",
    readState: true,
  },
  {
    id: 3,
    timestamp: "2025-04-28 9:45 AM",
    subject: "Welcome to the Team!",
    content: "We're excited to have you on board. Let us know if you need anything.",
    sender: "hr@example.com",
    recipient: "you@example.com",
    readState: true,
  },
];

export default function Inbox() {
  const [selectedMessage, setSelectedMessage] = useState<Message | null>(null);

  const handleSelectMessage = (message: Message) => {
    setSelectedMessage(message);
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="inbox-container">
        <div className="inbox-menu">
          <h2>Messages</h2>
          <ul>
            {sampleMessages.map((message) => (
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
            <div className="no-message-selected">Select a message to view its details</div>
          )}
        </div>
      </div>
    </Suspense>
  );
}