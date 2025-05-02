import React, { Suspense } from 'react';
import "app/styles/study-creation-page.css";

const CountNavigationBar = React.lazy(() => import('../components/Header/Header'));

/* File Holds Study Creation Form and Input Fields */
// function formatTime(timeStr){
//     const [hours, minutes] = timeStr.split(':');
//     const date = new Date();
//     date.setHours(parseInt(hours), parseInt(minutes));

//     return date.toLocaleTimeString('en-US', {
//     hour: 'numeric',
//     minute: '2-digit',
//     hour12: true
//     });
// }
export default function StudyCreationForm() {
  return (
    <div>
      <Suspense fallback={<div>Loading...</div>}>
        <CountNavigationBar />
      </Suspense>

      <div className="form-wrapper">
        <form
          className="study-group-creation"
          id="study-group-creation"
          onSubmit={(e) => e.preventDefault()}
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
              />
            </div>
            <br />

            {/* User selects course */}
            <div className="form-field">
              <label htmlFor="class-selection"><b>Course</b></label>
              <br />
              <select name="class-selection" id="class-selection">
                {/* TODO: Update which classes will be displayed */}
                <option value="" disabled selected>Select a course</option>
                <option value="CSCI 201">CSCI 201</option>
                <option value="CSCI 103">CSCI 103</option>
                <option value="CSCI 100">CSCI 100</option>
              </select>
            </div>
            <br />

            {/* User selects study topic */}
            <div className="form-field">
              <label htmlFor="study-goal"><b>Study Goal</b></label>
              <div
                id="study-goal"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
              >
                <button type="button" className="choice-button">Midterm</button>
                <button type="button" className="choice-button">Homework</button>
                <button type="button" className="choice-button">Labs</button>
                <button type="button" className="choice-button">Projects</button>
                <button type="button" className="choice-button">Add a new goal</button>
              </div>
            </div>
            <br />

            {/* User adds a group description */}
            <div className="form-field">
              <label htmlFor="study-description"><b>Description</b></label>
              <br />
              <textarea
                id="study-description-field"
                name="study-description"
                placeholder="Enter a short description of your study group"
                rows={5}
                cols={50}
              ></textarea>
            </div>
            <br />

            {/* User selects if Group is public or private */}
            <div className="form-field">
              <label htmlFor="privacy-settings"><b>Privacy Option</b></label>
              <div
                id="privacy-settings"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
              >
                <button type="button" className="choice-button">Private</button>
                <button type="button" className="choice-button">Public</button>
              </div>
            </div>
            <br/>

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
                <button type="button" className="choice-button">Zoom</button>
                <button type="button" className="choice-button">In-person</button>
            </div>
            </div>
            <br />

            {/* Set Meeting Time */}
            <div className="form-field">
            <label><b>Meeting Date & Time</b></label>
            <div style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}>
                {/* Date Picker */}
                <div>
                <label htmlFor="meeting-date">Date:</label><br />
                <input type="date" id="meeting-date" name="meeting-date" />
                </div>

                {/* Start Time */}
                <div>
                <label htmlFor="start-time">Start Time:</label><br />
                <input type="time" id="start-time" name="start-time" />
                </div>

                {/* End Time */}
                <div>
                <label htmlFor="end-time">End Time:</label><br />
                <input type="time" id="end-time" name="end-time" />
                </div>
            </div>
            </div>
            <br />

            {/* Selects frequency of meeting */}
            <div className="form-field">
            <label htmlFor="repeated-session"><b>Repeated Session</b></label>
            <div
                id="repeated-session"
                style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}
            >
                <button type="button" className="choice-button">None</button>
                <button type="button" className="choice-button">Bi-weekly</button>
                <button type="button" className="choice-button">Weekly</button>
                <button type="button" className="choice-button">Daily</button>
            </div>
            </div>
            <br></br>
            
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
                {/* Submit Button */}
                <button type="submit" className="submit-button">
                Submit
                </button>

                {/* Clear Button */}
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
