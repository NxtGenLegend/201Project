import React from 'react';
import './GroupCard.css';
import { FiCalendar, FiMapPin, FiUser } from 'react-icons/fi';

interface GroupCardProps {
  groupImage: string;
  groupName: string;
  classCode: string;
  className: string;
  meetingDayTime: string;
  meetingType: string;
  memberCount: number;
  groupLead: string;
}

const GroupCard: React.FC<GroupCardProps> = ({
  groupImage,
  groupName,
  classCode,
  className,
  meetingDayTime,
  meetingType,
  memberCount,
  groupLead,
}) => {
  return (
    <div className="group-card">
      <img src={groupImage} alt={groupName} className="group-image" />

      <div className="group-info">
        <h3 className="group-name">{groupName}</h3>
        <p className="class-name">{classCode} - {className}</p>

        <div className="group-details">
          <div className="detail-row">
            <FiCalendar className="detail-icon" />
            <span>{meetingDayTime}</span>
          </div>
          <div className="detail-row">
            <FiMapPin className="detail-icon" />
            <span>{meetingType}</span>
          </div>
          <div className="detail-row">
            <FiUser className="detail-icon" />
            <span>{memberCount} members</span>
          </div>
        </div>

        <div className="group-footer">
          <span className="created-by">Created by {groupLead}</span>
          <button className="explore-button">Explore</button>
        </div>
      </div>
    </div>
  );
};

export default GroupCard;