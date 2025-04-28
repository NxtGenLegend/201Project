import '../styles/Home.css';
import React, { Suspense, useState } from 'react';

const Header = React.lazy(() => import('../components/Header/Header'));

export default function Home() {
  // add state to control search input
  const [searchValue, setSearchValue] = useState('');
  // states to control filter status
  const [selectedClass, setSelectedClass] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [selectedMeetingOption, setSelectedMeetingOption] = useState('');

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <main style={{ padding: '1rem' }}>
        <div className="search-container">
          {/* search bar */}
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
      </main>
    </Suspense>
  );
}