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
  const [groupData, setGroupData] = useState({
    name: "Group Name",
    course: "CSCI 201 – Principles of Software Development",
    imageUrl: "/assets/default-group.png",
    time: "18:00", // 6:00 PM
    meetinPreference: "zoom", 
    numOfMembers: "8"
  });

  useEffect(() => {
    const groupId = new URLSearchParams(window.location.search).get("id");

    // TODO: Connect to backend to fetch data
    /*
    fetch(`/get-group-details?id=${groupId}`)
      .then(response => response.json())
      .then(data => setGroupData(data))
      .catch(error => console.error("Failed to load group data", error));
    */
  }, []);

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
          src={groupData.imageUrl}
        />
        <div className="group-banner-text">
          <h1><b>{groupData.name}</b></h1>
          <p>{groupData.course}</p>
        </div>
      </div>

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
            <p>{(groupData.meetinPreference)}</p>
          </div>
          <div className='detail-items'>
            {/* Shows Num of Members */}
            <p>{(groupData.numOfMembers)} members</p>
          </div>
        </div>

        {/* Right: Group Members */}
        <div className="group-members-container">
          <b>Group Members</b>
        </div>
      </div>
    </div>
  );
}
