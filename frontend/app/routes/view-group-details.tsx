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
  const [groupData, setGroupData] = useState<null | {
    name: string;
    course: string;
    imageUrl: string;
    time: string;
    meetinPreference: string;
    numOfMembers: string;
    nameOfMembers: string[];
    memberMajors: string[];
    memberYears: string[];
  }>(null);

  useEffect(() => {
    const groupId = new URLSearchParams(window.location.search).get("id");

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

        //stores group data
        const group = data.studyGroup;
        const isoTime = group.meetingTime?.slice(11, 16) || "18:00";

        setGroupData({
          name: group.groupName,
          course: group.course,
          imageUrl: "/assets/default-group.png",
          time: isoTime,
          meetinPreference: group.meetingType === "VIRTUAL" ? "zoom" : "in person",
          numOfMembers: (group.nameOfMembers?.length || 0).toString(),
          nameOfMembers: group.nameOfMembers || [],
          memberMajors: group.memberMajors || [],
          memberYears: group.memberYears || [],
        });
      })
      .catch((error) => {
        console.error("Failed to load group data:", error.message);
      });
  }, []);

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
          {/*Add Join/Request Button */}
          <div className="group-banner-buttons">
            <button className="banner-button">Request to Join</button>
            <button className="banner-button">Send Request</button>
          </div>
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
            <p>{groupData.meetinPreference}</p>
          </div>
          <div className='detail-items'>
            {/* Shows Num of Members */}
            <p>{groupData.numOfMembers} members</p>
          </div>
        </div>

        {/* Right: Group Members */}
        <div className="group-members-container">
          <b>Group Members</b>
          <div>
            {groupData.nameOfMembers.map((name, idx) => (
              <div className="member-row" key={idx}>
                {/* <div> */}
                <div className="name-col">{name}</div>
                <div className='student-cont'>
                  <div className="major-col">{groupData.memberMajors[idx]}</div>
                  <div className="year-col">{groupData.memberYears[idx]}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
