package com.study_group_matcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InboxDTO {
    private Long messageId;
    private Long invitationId;
}