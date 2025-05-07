import React, { Suspense, useEffect, useState } from 'react';
import "app/styles/view-group-details.css";
import "app/styles/study-creation-page.css";

const CountNavigationBar = React.lazy(() => import('../components/Header/Header'));

// Formats time from "HH:MM" to "6:00 PM"
function formatTime(timeStr: string) {
  const [hours, minutes] = timeStr.split(':');
  const date = new Date();
  
  date.setHours(parseInt(hours), parseInt(minutes));

  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
}

export default function ViewStudyGroupDetails() {
  // Dummy Data

  // Tracks selected Button to bold choice
  const [selectedAction, setSelectedAction] = useState("");
  // Sets email to send a request to join the group
  const [sendRequest, setSendRequest] = useState("");
  // Sets the email pop up, user is able to enter email 
  const [displayEmailPopup, setDisplayEmailPopup] = useState(false);
  // Sets pop up to send a message
  const [displayMessagePopup, setDisplayMessagePopup] = useState(false);
  // Sets to select user to send message and the text
  const [selectedRecipient, setSelectedRecipient] = useState("");
  const [messageText, setMessageText] = useState(""); 
  const [groupData, setGroupData] = useState<null | {
    name: string;
    course: string;
    imageUrl: string;
    time: string;
    meetinPreference: string;
    numOfMembers: number;
    nameOfMembers: string[];
    memberMajors: string[];
    memberYears: string[];
  }>(null);

  useEffect(() => {
    const groupId = new URLSearchParams(window.location.search).get("id"); console.log("Group ID from URL:", groupId);

    //Connect to backend to fetch data
    if (!groupId) {
      console.error("No group ID found in URL");
      return;
    }
    //Makes backend API call
    fetch(`http://localhost:8080/studygroup/${groupId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch group data");
        }
        return response.json();
      })
      .then((data) => {
        if (!data.success) {
          console.error("Backend error:", data.message);
          return;
        }
        
        console.log("Fetched group data:", data.studyGroup);
        console.log("Was able to fetch data");

        //stores group data
        const group = data.studyGroup;
        const isoTime = group.meetingTime?.slice(11, 16) || "18:00";
        const members = group.nameOfMembers ?? [];

        setGroupData({
          name: group.groupName,
          course: group.course,
          imageUrl: "/assets/default-group.png",
          time: isoTime,
          meetinPreference: group.meetingType === "VIRTUAL" ? "zoom" : "in person",
          numOfMembers:  members.length,
          nameOfMembers: group.nameOfMembers || [],
          memberMajors: group.memberMajors || [],
          memberYears: group.memberYears || [],
        });
      })
      .catch((error) => {
        console.error("Failed to load group data:", error.message);
      });
  }, []);

  const handleSendMessageClick = (name: string) => {
    setSelectedRecipient(name);
    setDisplayMessagePopup(true);
  };

  const handleSendMessage = () => {
    // Handle the actual message send here (e.g., API call)
    console.log("Message sent to:", selectedRecipient);
    console.log("Message:", messageText);

    // Reset popup
    setDisplayMessagePopup(false);
    setMessageText("");
    setSelectedRecipient("");
  };

  if (!groupData) {
    return <div>Loading group data...</div>;
  }
  return (
    <div>
      <Suspense fallback={<div>Loading...</div>}>
        <CountNavigationBar />
      </Suspense>

      {/* Group image with overlay text */}
      <div className="group-banner">
        <img
          alt="default group"
          className="group-image"
          src="assets/usc_banner.jpeg"
        />
        <div className="group-banner-text">
          <h1><b>{groupData.name}</b></h1>
          <p>{groupData.course}</p>
          {/* TODO: Update logic so admin can see send request and User can send Request to Join */}
          {/* Add Join/Request Button */}
          <div className="group-banner-buttons" style={{ display: "flex", gap: "20px", color: "black", height: "50px" }}>
            <button
              type="button"
              className={`choice-button ${selectedAction === "JOIN" ? "selected-button" : ""}`}
              onClick={() => setSelectedAction("JOIN")}
            >
              Request to Join
            </button>
            <button
              type="button"
              className={`choice-button ${selectedAction === "SEND" ? "selected-button" : ""}`}
              onClick={() => {
                setSelectedAction("SEND");
                setDisplayEmailPopup(true);
              }}
            >
              Send Request
            </button>
          </div>
        </div>
      </div>

      {/* Show Email Popup if admin wants to send a request for a user to join */}
      {displayEmailPopup && (
        <div className="popup-container">
          <div className="popup-input">
            <b style={{ justifyContent: "left", display: "flex" }}>
              Send email to request a user to join
            </b>
            <label htmlFor="invite-email"></label>
            <input
              type="email"
              id="invite-email"
              name="invite-email"
              placeholder="Enter email"
              style={{ padding: "8px", width: "100%", marginTop: "10px" }}
            /><br></br><br></br>
            {/* Sets cancel or confirm buttons for request popup contsiner */}
            <div className="group-banner-buttons" style={{ display: "flex", gap: "20px", color: "black", justifyContent: "center"}}>
              {/* Set Button to cancel request */}
              <button
                type="button"
                className={`choice-button ${selectedAction === "CANCEL" ? "selected-button" : ""}`}
                onClick={() => {
                  setSelectedAction("CANCEL");
                  setDisplayEmailPopup(false);
                }}
              >
                Cancel
              </button>

              {/* Set Button to submit request */}
              <button
                type="button"
                className={`choice-button ${selectedAction === "CONFIRM" ? "selected-button" : ""}`}
                onClick={() => {
                  setSelectedAction("CONFIRM");
                  setDisplayEmailPopup(true);
                }}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    
      {/* Displays details */}
      <div
        style={{
          display: "flex",
          gap: "30px",
          justifyContent: "flex-start",
          flexWrap: "wrap",
          marginTop: "20px",
          padding: "30px"
        }}
      >
        {/* Left: Study Group Details */}
        <div className="group-details-container">
          <b>Group Details</b>
          <div className='detail-items'>
            <p>Meeting Time: {formatTime(groupData.time)}</p>
          </div>
          <div className='detail-items'>
            {/* Shows Preference */}
            <p>{groupData.meetinPreference}</p>
          </div>
          <div className='detail-items'>
            {/* Shows Num of Members */}
            <p>{groupData.numOfMembers} members</p>
          </div>
        </div>

        {/* Right: Group Members */}
        <div className="group-members-container">
          <b style={{ fontSize: "24px" }}>Group Members</b><br /><br />
          <div>
            {groupData.nameOfMembers.map((name, idx) => (
              <div className="member-row" key={idx}>
                {/* Member name */}
                <div className="name-col">{name}</div>
                <div className='student-cont'>
                  <div className="major-col">{groupData.memberMajors[idx]}</div>
                  <div className="year-col">{groupData.memberYears[idx]}</div>
                </div>
                {/* Sets button to allow to send message to user */}
                <div className="name-col">
                  <button
                    style={{ color: "black", justifyContent: "center", width: "200px", height: "40px"}}
                    type="button"
                    className="choice-button"
                    onClick={() => handleSendMessageClick(name)}
                  >
                    Send message
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Message popup - moved outside the member mapping */}
      {displayMessagePopup && (
        <div className="popup-container">
          <div className="popup-input">
            <label htmlFor="message-text"><b>Send Message to {selectedRecipient}</b></label>
            <textarea
              className="message-text"
              id="message-text"
              name="message-text"
              placeholder="Type your message here"
              value={messageText}
              onChange={(e) => setMessageText(e.target.value)}
              rows={5}
              style={{ padding: "8px", width: "100%", marginTop: "10px", height: "100%", color: "black"}}
            />
            
            <br /><br />
            <div className="group-banner-buttons" style={{ display: "flex", gap: "20px", justifyContent: "center" }}>
              <button
                type="button"
                className="choice-button"
                onClick={() => setDisplayMessagePopup(false)}
              >
                Cancel
              </button>
              <button
                type="button"
                className="choice-button"
                onClick={handleSendMessage}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}