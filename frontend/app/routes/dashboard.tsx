import React, { Suspense, useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/Dashboard.css';
import { useNavigate } from 'react-router-dom';

const Header = React.lazy(() => import('../components/Header/Header'));
const GroupCard = React.lazy(() => import('../components/GroupCard/GroupCard'));

const Dashboard: React.FC = () => {
    const [groups, setGroups] = useState<any[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGroups = async () => {
            try {
                const response = await axios.get('http://localhost:8080/studygroup/user/1/groups');
                const groups = response.data.studyGroups;
                setGroups(groups);
            } catch (err) {
                console.error('Failed to fetch groups:', err);
            }
        };
        fetchGroups();
    }, []);

    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Header />
            <main style={{ padding: '1rem', backgroundColor: "#f5f5f5" }}>
                <div className="banner-section">
                    <img src="/assets/usc_banner.jpeg" alt="USC Banner" className="banner-image" />
                    <div className="banner-overlay" />
                    <div className="banner-text">Welcome to Study Group Finder!</div>
                </div>

                <div className="dashboard-container">
                    <div className="dashboard-content">
                        <div className="study-groups-section">
                            <h2>My Study Groups</h2>
                            <div className="study-group-cards"
                            onClick={() => navigate(`/viewDetails?id=1`)}>
                                {groups.map((group, index) => (
                                    <GroupCard
                    
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

                        <div className="calendar-section">
                            <h2>My Calendar</h2>
                            <div className="calendar-list">
                                {groups.map((group, idx) => (
                                    <div key={idx} className="calendar-item">
                                        <div className="calendar-date">
                                            <div className="day">{new Date(group.meetingTime).toLocaleDateString('en-US', { weekday: 'short' }).toUpperCase()}</div>
                                            <div className="full-date">{new Date(group.meetingTime).toLocaleDateString()}</div>
                                        </div>
                                        <div className="calendar-details">
                                            <div className="meeting-time">{new Date(group.meetingTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
                                            <div className="meeting-title">{group.group_name} Weekly Meeting</div>
                                            <div className="course-name">{group.course}</div>
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
