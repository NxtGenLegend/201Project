import React, { Suspense, useState } from 'react';
import "app/styles/study-creation-page.css";

const CountNavigationBar = React.lazy(() => import('../components/Header/Header'));

export default function StudyCreationForm() {
  const [groupName, setGroupName] = useState("");
  const [course, setCourse] = useState("");
  const [privacy, setPrivacy] = useState("");
  const [meetingType, setMeetingType] = useState("");
  const [meetingDate, setMeetingDate] = useState("");
  const [meetingStartTime, setMeetingStartTime] = useState("");
  const [location, setLocation] = useState("");

  //TODO: Need to update adminId to cookies, email
  const adminId = 1; 

  //When user submits form, handles event to send input collected to servlet
  async function handleSubmit(e: { preventDefault: () => void; }) {
    e.preventDefault(); 

    //Sets meeting time in format for backend
    const meetingTime = `${meetingDate}T${meetingStartTime}`;

    //Debug, checks the user collected date and time 
    if (!meetingDate || !meetingStartTime) {
      alert("Please select both a date and start time.");
      return;
    }

    //Builds request body for servlet, gathers input from form fields
    const reqBody = {
      adminID: adminId, 
      groupName, 
      course, 
      meetingTime, 
      meetingType,
      location, 
      privacy
    };

    //Sends Request body to backend
    try {
      const response = await fetch("studygroup/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(reqBody)
      });

      const result = await response.json();

      if (result.success) {
        alert("Study group created with Group ID: " + result.groupID);
      } else {
        alert("Failed to create group: " + result.message);
      }
    } catch (error) {
      alert("Could not submit the form. Please try again.");
      console.error("Error submitting form:", error);
    }
  }

  return (
    <div>
      <Suspense fallback={<div>Loading...</div>}>
        <CountNavigationBar />
      </Suspense>

      <div className="form-wrapper">
        <form
          className="study-group-creation"
          id="study-group-creation"
          onSubmit={handleSubmit}
        >
          {/* Sets study container design */}
          <div id="form-container" className="form-container">
            <h2 style={{ fontSize: "30px", color: "#770E04" }}>
              <b>Create a Study Group</b>
            </h2>
            <br />

            {/* User enters group name */}
            <div className="form-field">
              <label htmlFor="group-name"><b>Group Name</b></label>
              <br />
              <input
                type="text"
                id="group-name-field"
                name="group-name"
                placeholder="Enter group name here"
                value={groupName}
                onChange={(e) => setGroupName(e.target.value)}
              />
            </div>
            <br />

            {/* User selects course */}
            <div className="form-field">
              <label htmlFor="class-selection"><b>Course</b></label>
              <br />
              <select
                name="class-selection"
                id="class-selection"
                value={course}
                onChange={(e) => setCourse(e.target.value)}
              >
                {/* TODO: Update which classes will be displayed */}
                <option value="" disabled>Select a course</option>
                <option value="CSCI 201">CSCI 201</option>
                <option value="CSCI 103">CSCI 103</option>
                <option value="CSCI 104">CSCI 104</option>
                <option value="CSCI 270">CSCI 270</option>
                <option value="CSCI 310">CSCI 310</option>
              </select>
            </div>
            <br />

            {/* User selects if Group is public or private */}
            <div className="form-field">
              <label htmlFor="privacy-settings"><b>Privacy Option</b></label>
              <div
                id="privacy-settings"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
              >
                <button
                  type="button"
                  className={`choice-button ${privacy === "PRIVATE" ? "selected-button" : ""}`}
                  onClick={() => setPrivacy("PRIVATE")}
                >
                  Private
                </button>
                <button
                  type="button"
                  className={`choice-button ${privacy === "PUBLIC" ? "selected-button" : ""}`}
                  onClick={() => setPrivacy("PUBLIC")}
                >
                  Public
                </button>
              </div>
            </div>
            <br />

            {/* Meeting Details Section */}
            <div className="form-field">
              <h2 style={{ fontSize: "23px", color: "#770E04" }}>
                <b>Meeting Details</b>
              </h2>
              <br />
            </div>

            {/* Location Preference Selection Choice */}
            <div className="form-field">
              <label htmlFor="meeting-preference"><b>Meeting Preference</b></label>
              <div
                id="meeting-preference"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
              >
                <button
                  type="button"
                  className={`choice-button ${meetingType === "VIRTUAL" ? "selected-button" : ""}`}
                  onClick={() => setMeetingType("VIRTUAL")}
                >
                  Zoom
                </button>
                <button
                  type="button"
                  className={`choice-button ${meetingType === "IN_PERSON" ? "selected-button" : ""}`}
                  onClick={() => setMeetingType("IN_PERSON")}
                >
                  In-person
                </button>
              </div><br />
              {/* Select location and Preference buttons */}
              <label htmlFor="meeting-location"><b>Meeting Location</b></label>
              <div
                id="meeting-location"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
              >
                <button
                  type="button"
                  className={`choice-button ${location === "Leavy Library" ? "selected-button" : ""}`}
                  onClick={() => setLocation("Leavy Library")}
                >
                  Leavy Library
                </button>
                <button
                  type="button"
                  className={`choice-button ${location === "Student Lounge" ? "selected-button" : ""}`}
                  onClick={() => setLocation("Student Lounge")}
                >
                  Student Lounge
                </button>
                <button
                  type="button"
                  className={`choice-button ${location === "Class" ? "selected-button" : ""}`}
                  onClick={() => setLocation("Class")}
                >
                  Class
                </button>
                <button
                  type="button"
                  className={`choice-button ${location === "Doheny Library" ? "selected-button" : ""}`}
                  onClick={() => setLocation("Doheny Library")}
                >
                  Doheny Library
                </button>
              </div>
            </div>
            <br />

            {/* Set Meeting Time */}
            <div className="form-field">
              <label><b>Meeting Date & Time</b></label>
              <div style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}>
                <input
                  type="date"
                  id="meeting-date"
                  name="meeting-date"
                  value={meetingDate}
                  onChange={(e) => setMeetingDate(e.target.value)}
                />
                <input
                  type="time"
                  id="start-time"
                  name="start-time"
                  value={meetingStartTime}
                  onChange={(e) => setMeetingStartTime(e.target.value)}
                />
              </div>
            </div>
            <br />

            {/* Invite a classroom option */}
            <div className="form-field">
              <label htmlFor="invite-email"><b>Invite Classmate</b></label>
              <br />
              <input
                type="email"
                id="invite-email"
                name="invite-email"
                placeholder="Enter email"
              />
            </div>

            {/* Submit and Clear buttons */}
            <div className="form-field">
              <div style={{
                display: "flex",
                gap: "20px",
                flexWrap: "wrap",
                justifyContent: "center"
              }}>
                <button type="submit" className="submit-button">Submit</button>
                <button
                  type="button"
                  className="clear-button"
                  onClick={() => {
                    const form = document.getElementById("study-group-creation") as HTMLFormElement | null;
                    if (form) {
                      form.reset();
                    }
                  }}
                >
                  Clear
                </button>
              </div>
            </div>

          </div> {/* End of form-container */}
        </form>
      </div> {/* End of form-wrapper */}
    </div>
  );
}