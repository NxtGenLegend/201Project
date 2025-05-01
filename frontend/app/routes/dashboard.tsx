import React, { Suspense } from 'react';
import '../styles/Dashboard.css';

// import ui components
const Header = React.lazy(() => import('../components/Header/Header'));
const GroupCard = React.lazy(() => import('../components/GroupCard/GroupCard'));

const Dashboard: React.FC = () => {
    // TODO: replace dummy data with actual data from database
    const calendarData = [
        { date: 'March 31', time: '6:00 PM', title: 'Group Name Weekly Meeting', course: 'CSCI 201 – Principles of Software Development' },
        { date: 'April 7', time: '6:00 PM', title: 'Group Name Weekly Meeting', course: 'CSCI 201 – Principles of Software Development' },
        { date: 'April 14', time: '6:00 PM', title: 'Group Name Weekly Meeting', course: 'CSCI 201 – Principles of Software Development' },
    ];

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
        }
    ]

    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Header />
            <main style={{ padding: '1rem', backgroundColor: "#f5f5f5"}}>
                <div className="banner-section">
                    <img src="/assets/usc_banner.jpeg" alt="USC Banner" className="banner-image" />
                    <div className="banner-overlay" />
                    <div className="banner-text">Welcome to Study Group Finder!</div>
                </div>
                <div className="dashboard-container">
                    <div className="dashboard-content">
                        <div className="study-groups-section">
                            <h2>My Study Groups</h2>
                            <div className="study-group-cards">
                                {dummyGroups.map((group, index) => (
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

                        <div className="calendar-section">
                            <h2>My Calendar</h2>
                            <div className="calendar-list">
                                {calendarData.map((item, idx) => (
                                    <div key={idx} className="calendar-item">
                                        <div className="calendar-date">
                                            <div className="day">MON</div>
                                            <div className="full-date">{item.date}</div>
                                        </div>
                                        <div className="calendar-details">
                                            <div className="meeting-time">{item.time}</div>
                                            <div className="meeting-title">{item.title}</div>
                                            <div className="course-name">{item.course}</div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </Suspense>
    );
};

export default Dashboard;