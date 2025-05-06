import React from 'react';
import './GroupCard.css';
import { FiCalendar, FiMapPin, FiUser } from 'react-icons/fi';
import groupImage from '../../../public/assets/default_group.png'

interface GroupCardProps {
  groupName: string;
  course: string;
  meetingTime: string;
  meetingType: string; // "IN_PERSON" or "VIRTUAL"
  location: string;
  privacy: string; // "PUBLIC" or "PRIVATE"
}

const GroupCard: React.FC<GroupCardProps> = ({
  groupName,
  course,
  meetingTime,
  meetingType,
  location,
  privacy
}) => {
  return (
    <div className="group-card">
      <img src={groupImage} alt={groupName} className="group-image" />

      <div className="group-info">
        <h3 className="group-name">{groupName}</h3>
        <p className="class-name">{course}</p>

        <div className="group-details">
          <div className="detail-row">
            <FiCalendar className="detail-icon" />
            <span>{meetingTime}</span>
          </div>
          <div className="detail-row">
            <FiMapPin className="detail-icon" />
            <span>{meetingType}</span>
          </div>
          <div className="detail-row">
            <FiUser className="detail-icon" />
            <span>Meet at {location}</span>
          </div>
        </div>

        <div className="group-footer">
          {/* <span className="created-by">Created by {}</span> */}
          <button className="explore-button">Explore</button>
        </div>
      </div>
    </div>
  );
};

export default GroupCard;