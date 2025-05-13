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
//Helper fucntion to call API endpoint to convert a username to a userId
export async function getUserIdByUsername(username: string): Promise<number | null> {
  try {
    const response = await fetch(`http://localhost:8080/api/users/by-username?username=${encodeURIComponent(username)}`);
    if (!response.ok) throw new Error("User not found");
    const userId = await response.json();
    console.log("This is the userId found: " + userId); 
    return userId;
  } catch (error) {
    console.error("Failed to fetch user ID:", error);
    return null;
  }
}
export default function ViewStudyGroupDetails() {
  
  const [adminId, setAdminId] = useState<number | null>(null);
  // Tracks selected Button to bold choice
  const [selectedAction, setSelectedAction] = useState("");
  // Sets email to send a request to join the group
  const [sendRequest, setSendRequest] = useState("");
  // Sets the email pop up, user is able to enter email 
  const [displayEmailPopup, setDisplayEmailPopup] = useState(false);
  // Sets pop up to send a message
  const [displayMessagePopup, setDisplayMessagePopup] = useState(false);
  const [messageText, setMessageText] = useState("");

  const [inviteEmail, setInviteEmail] = useState("");
  //const adminId = 1; // Replace with actual user from context/session

  //Checks if the user is logged in
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  useEffect(() => {
    if (typeof window !== "undefined") {
      const loggedIn = localStorage.getItem("isLoggedIn") === "true";
      setIsLoggedIn(loggedIn);
    }
  }, []);

  // Sets to select user to send message and the text
  const [selectedRecipient, setSelectedRecipient] = useState<null | {
    userId: number;
    name: string;
    major: string;
    year: string;
  }>(null);

  const [groupData, setGroupData] = useState<null | {
    name: string;
    course: string;
    imageUrl: string;
    time: string;
    meetinPreference: string;
    numOfMembers: number; 
    adminId: number; 
    members: {
      userId: number;
      name: string;
      major: string;
      year: string;
    }[];
  }>(null);

  useEffect(() => {
    const fetchGroupData = async () => {
      const groupId = new URLSearchParams(window.location.search).get("id"); 
      console.log("Group ID from URL:", groupId);

      //Connect to backend to fetch data
      if (!groupId) {
        console.error("No group ID found in URL");
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/studygroup/${groupId}`);
        const data = await response.json();

        if (!data.success) {
          console.error("Backend error:", data.message);
          return;
        }

        console.log("Fetched group data:", data.studyGroup);
        const group = data.studyGroup;
        const isoTime = group.meetingTime?.slice(11, 16) || "18:00";
        const members = group.members ?? []

        setGroupData({
          name: group.groupName,
          course: group.course,
          imageUrl: "/assets/default-group.png",
          time: isoTime,
          meetinPreference: group.meetingType === "VIRTUAL" ? "zoom" : "in person",
          numOfMembers: (group.members || []).length,
          adminId: group.adminID,
          members: (group.members || []).map((m: any, i: number) => ({
            userId: m.userId,
            name: m.name,
            major: group.memberMajors?.[i] || "Computer Science",
            year: group.memberYears?.[i] || "Freshman"
          }))
        });

        setAdminId(group.adminID); 

      } catch (error) {
        console.error("Failed to load group data:", error);
      }
    };

    fetchGroupData();
  }, []);

  const handleSendMessageClick = (member: {
    userId: number;
    name: string;
    major: string;
    year: string;
  }) => {
    setSelectedRecipient(member);
    setDisplayMessagePopup(true);
  };


  const handleSendMessage = async () => {
    if (!selectedRecipient || !messageText.trim()) {
      console.warn("No recipient or message text");
      return;
    }
  
    const senderUsername = localStorage.getItem("username");
    if (!senderUsername) {
      alert("You must be logged in to send a message.");
      return;
    }
  
    const senderId = await getUserIdByUsername(senderUsername);
    if (!senderId) {
      alert("Failed to resolve sender ID.");
      return;
    }
  
    //Send the message first
    try {
      const messageRes = await fetch("http://localhost:8080/api/messages/dm", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          senderId: senderId,
          recipientId: selectedRecipient.userId,
          messageBody: messageText
        })
      });
  
      if (!messageRes.ok) throw new Error("Failed to send message");
  
      const messageId = await messageRes.json(); // assuming backend returns messageId
  
      // 2. Add message to Inbox for the recipient
      const inboxRes = await fetch("http://localhost:8080/api/inbox/inbox", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          user_id: selectedRecipient.userId,
          sender_id: senderId,
          message_id: messageId,
          invitation_id: 0  // or null if not applicable
        })
      });
  
      if (!inboxRes.ok) throw new Error("Failed to add message to inbox");
  
      alert("Message sent and added to inbox.");
      setDisplayMessagePopup(false);
      setMessageText("");
      setSelectedRecipient(null);
  
    } catch (error) {
      console.error("Error sending message or adding to inbox:", error);
      alert("An error occurred. Message may not have been sent.");
    }
  };


  const handleSendInvite = async () => {
    const groupId = new URLSearchParams(window.location.search).get("id");
    const adminId = 1; // TODO: replace with logged-in admin ID
  
    if ( !groupId) {
      alert("Missing email or group ID");
      return;
    }
  
    try {
      const res = await fetch(`http://localhost:8080/api/invitations?creatorId=${adminId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          groupId: groupId,
          recipientId: parseInt(inviteEmail) //userId instead of email
        })
      });
  
      const data = await res.json();
      if (res.ok) {
        alert("Invitation sent successfully.");
        setDisplayEmailPopup(false);
      } else {
        alert("Failed to send invitation. "
          + "Need to be an admin to send invite");
      }
    } catch (error) {
      console.error("Error sending invite:", error);
      alert("Error sending invitation.");
    }
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

          {/* Add Join/Request Button */}
          {/* Only show send request or request to join if user is logged in */}
          {isLoggedIn && (
          <div className="group-banner-buttons" style={{ display: "flex", gap: "20px", color: "black", height: "50px" }}>
          
            <button
              type="button"
              className={`choice-button ${selectedAction === "SEND" ? "selected-button" : ""}`}
              onClick={() => {
                setSelectedAction("SEND");
                setDisplayEmailPopup(true);
              }}
            >
              Add a user
            </button>
          </div>
          )}
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
              type="number"
              id="invite-email"
              name="invite-email"
              placeholder="Enter userId"
              style={{ padding: "8px", width: "100%", marginTop: "10px" }}
              value={inviteEmail || ''}
              onChange={(e) => setInviteEmail(e.target.value)}
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
                onClick={handleSendInvite}
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
          
          {groupData.members.map((member, idx) => (
              <div className="member-row" key={idx}>
                {/* Member name */}
                {/* <div className="name-col">{member.userId}</div> */}
                <div className="name-col">{member.name}</div>
                <div className='student-cont'>
                  <div className="major-col">{member.major}</div>
                  <div className="year-col">{member.year}</div>
                </div>
                {/* Sets button to allow to send message to user */}
                <div className="name-col">


                {isLoggedIn && (
                <>
                <button
                  style={{ color: "black", justifyContent: "center", width: "200px", height: "40px"}}
                  type="button"
                  className="choice-button"
                  onClick={() => handleSendMessageClick(member)}
                >
                  Send message
                </button>
                </>
                )}
                
                </div>
              </div>
            ))}


          </div>
        </div>
      </div>

      {/* Message popup - moved outside the member mapping */}
      {/* TODO: Connect to Backend to send messages */}
      {displayMessagePopup && (
        <div className="popup-container">
          <div className="popup-input">
          <label htmlFor="message-text"><b>Send Message to {selectedRecipient?.name}</b></label>
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