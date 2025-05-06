import '../styles/Home.css';
import React, { Suspense, useState, useEffect } from 'react';
import axios from 'axios';

// import ui components
const Header = React.lazy(() => import('../components/Header/Header'));
const GroupCard = React.lazy(() => import('../components/GroupCard/GroupCard'));

interface StudyGroup {
  groupID: number;
  adminID: number;
  groupName: string;
  course: string; // replace classCode/className
  meetingTime: string; // ISO string
  meetingType: string; // "IN_PERSON" or "VIRTUAL"
  location: string;
  privacy: string; // "PUBLIC" or "PRIVATE"
}

export default function Home() {
  const [groups, setGroups] = useState<StudyGroup[]>([]);

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const response = await axios.get('http://10.25.117.46:8080/studygroup/all');
        const groups = response.data.studyGroups;
        setGroups(groups); // assuming response is already a list of StudyGroup
      } catch (error) {
        const err = error as any;
        console.error('Failed to fetch study groups:', err.response?.data || err.message);
      }
    };

    fetchGroups();
  }, []);

  // add state to control search input
  const [searchValue, setSearchValue] = useState('');

  // states to control filter status
  const [selectedClass, setSelectedClass] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [selectedMeetingOption, setSelectedMeetingOption] = useState('');

  // array to store search and filter results
  const filteredGroups = groups.filter((group) => {
    // groups with name that contains the searched term
    let matchesSearch = (searchValue === '') || group.groupName.toLowerCase().includes(searchValue.toLowerCase());
    // groups with the exact same class code as selected
    let matchesClass = (selectedClass === '') || (group.course === selectedClass);
    // groups with the same meeting time as selected
    let matchesTime = (selectedTime === '') || group.meetingTime.toLowerCase().includes(selectedTime.toLowerCase());
    // groups with the exact same meeting option as selected
    let matchesMeetingOption = (selectedMeetingOption === '') || (group.meetingType.toLowerCase() === selectedMeetingOption.toLowerCase());

    // only return results that match ALL criteria
    return matchesSearch && matchesClass && matchesTime && matchesMeetingOption;
  });

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <main style={{ padding: '1rem', backgroundColor: "#f5f5f5" }}>
        {/* search bar */}
        <div className="search-container">
          <input
            type="text"
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            placeholder="Search..."
            aria-label="Search input"
            className="search-input"
          />
        </div>
        {/* filters */}
        <div className="filter-group">
          {/* class */}
          <select
            value={selectedClass}
            onChange={(e) => setSelectedClass(e.target.value)}
            className="filter-dropdown"
          >
            {/* TODO: change this later to reflect all classes */}
            <option value="">Select Class Option</option>
            <option value="CSCI 102">CSCI 102</option>
            <option value="CSCI 103">CSCI 103</option>
            <option value="CSCI 104">CSCI 104</option>
            <option value="CSCI 201">CSCI 201</option>
            <option value="CSCI 270">CSCI 270</option>
          </select>

          {/* meeting time */}
          <select
            value={selectedTime}
            onChange={(e) => setSelectedTime(e.target.value)}
            className="filter-dropdown"
          >
            <option value="">Select Meeting Time</option>
            <option value="Monday">Monday</option>
            <option value="Tuesday">Tuesday</option>
            <option value="Wednesday">Wednesday</option>
            <option value="Thursday">Thursday</option>
            <option value="Friday">Friday</option>
          </select>

          {/* meeting option */}
          <select
            value={selectedMeetingOption}
            onChange={(e) => setSelectedMeetingOption(e.target.value)}
            className="filter-dropdown"
          >
            <option value="">Select Meeting Option</option>
            <option value="In Person">In Person</option>
            <option value="Virtual">Virtual</option>
          </select>
        </div>
        {/* group cards */}
        <div className="group-cards-wrapper">
          <div className="group-cards-container">
            {filteredGroups.map((group, index) => (
              <GroupCard
                key={index}
                groupName={group.groupName}
                course={group.course}
                meetingTime={group.meetingTime}
                meetingType={group.meetingType}
                location={group.location}
                privacy={group.privacy}
              />
            ))}
          </div>
        </div>

      </main>
    </Suspense>
  );
}