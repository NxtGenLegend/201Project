import '../styles/Home.css';
import React, { Suspense, useState } from 'react';

// import ui components
const Header = React.lazy(() => import('../components/Header/Header'));
const GroupCard = React.lazy(() => import('../components/GroupCard/GroupCard'));

// TODO: fetch data from backend inside a useEffect
export default function Home() {
  // TODO: replace dummy data with actual data from database
  const dummyGroups = [
    {
      groupImage: "/assets/default_group.png",
      groupName: "The Debug Squad",
      classCode: "CSCI 104",
      className: "Data Structures and Object Oriented Design",
      meetingDayTime: "Monday @ 5PM",
      meetingType: "In Person",
      memberCount: 5,
      groupLead: "Tommy Trojan",
    },
    {
      groupImage: "/assets/default_group.png",
      groupName: "Algorithmic Avengers",
      classCode: "CSCI 270",
      className: "Introduction to Algorithms and Theory of Computing",
      meetingDayTime: "Wednesday @ 3PM",
      meetingType: "Virtual",
      memberCount: 8,
      groupLead: "John Doe",
    },
    {
      groupImage: "/assets/default_group.png",
      groupName: "Algo Warriors",
      classCode: "CSCI 270",
      className: "Introduction to Algorithms and Theory of Computing",
      meetingDayTime: "Wednesday @ 3PM",
      meetingType: "Virtual",
      memberCount: 8,
      groupLead: "Jane Doe",
    },
    {
      groupImage: "/assets/default_group.png",
      groupName: "Software Engineers",
      classCode: "CSCI 201",
      className: "Principles of Software Development",
      meetingDayTime: "Thursday @ 6PM",
      meetingType: "In Person",
      memberCount: 6,
      groupLead: "John Smith",
    }
  ];

  // add state to control search input
  const [searchValue, setSearchValue] = useState('');

  // states to control filter status
  const [selectedClass, setSelectedClass] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [selectedMeetingOption, setSelectedMeetingOption] = useState('');

  // array to store search and filter results
  const filteredGroups = dummyGroups.filter((group) => {
    // groups with name that contains the searched term
    let matchesSearch = (searchValue === '') || group.groupName.toLowerCase().includes(searchValue.toLowerCase());
    // groups with the exact same class code as selected
    let matchesClass = (selectedClass === '') || (group.classCode === selectedClass);
    // groups with the same meeting time as selected
    let matchesTime = (selectedTime === '') || group.meetingDayTime.toLowerCase().includes(selectedTime.toLowerCase());
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
                groupImage={group.groupImage}
                groupName={group.groupName}
                classCode={group.classCode}
                className={group.className}
                meetingDayTime={group.meetingDayTime}
                meetingType={group.meetingType}
                memberCount={group.memberCount}
                groupLead={group.groupLead}
              />
            ))}
          </div>
        </div>

      </main>
    </Suspense>
  );
}