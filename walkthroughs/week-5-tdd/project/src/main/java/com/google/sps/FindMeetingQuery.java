// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.*;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        long duration = request.getDuration();
        int start = TimeRange.getTimeInMinutes(0, 0);
        int end = TimeRange.getTimeInMinutes(0, 0);
        int lastEnd = TimeRange.getTimeInMinutes(0, 0);

        // First get the time ranges that work for all attendees. 
        List<TimeRange> blockedTimeRangesWithAllAttendees = 
            getBlockedEventTimeRanges(events, request.getAllAttendees());
        Collection<TimeRange> meetingTimeRangesWithAllAttendees = 
            getMeetingTimeRanges(blockedTimeRangesWithAllAttendees, duration);

        // If there are no time ranges that work for all attendees, get the time
        // ranges that work for only the mandatory attendees.
        // In the case that there are optional attendees but no mandatory attendees, 
        // treat the optional attendees as the mandatory attendees
        if (meetingTimeRangesWithAllAttendees.size() == 0 && request.getAttendees().size() != 0) {
            List<TimeRange> blockedTimeRangesWithMandatoryAttendees = 
                getBlockedEventTimeRanges(events, request.getAttendees());
            Collection<TimeRange> meetingTimeRangesWithMandatoryAttendees = 
                getMeetingTimeRanges(blockedTimeRangesWithMandatoryAttendees, duration);
            return meetingTimeRangesWithMandatoryAttendees;
        }else{
            return meetingTimeRangesWithAllAttendees;
        }
    }

    // Get all the blocked event time ranges and sort them by the start time. 
    // Only add to the list if at least one attendee from the event is 
    // a required attendee in the meeting request. (Otherwise, the event is irrelevant.)
    public List<TimeRange> getBlockedEventTimeRanges(Collection<Event> events, Collection<String> requestAttendees) {
        List<TimeRange> blockedTimeRanges = events.stream().filter(
            event -> event.getAttendees().stream().anyMatch(
            attendee -> requestAttendees.contains(attendee)))
            .map(e -> e.getWhen())
            .collect(Collectors.toList());
        Collections.sort(blockedTimeRanges, TimeRange.ORDER_BY_START);
        return blockedTimeRanges;
    }

    // Function that returns all valid time ranges based on a list of blocked time ranges
    // Approach: start from start of day, and add available timeranges as we traverse through the events' timeranges
    public Collection<TimeRange> getMeetingTimeRanges(List<TimeRange> blockedTimeRanges, long duration) {
        Collection<TimeRange> meetingTimeRanges = new ArrayList();
        int start = TimeRange.getTimeInMinutes(0, 0);
        int end = TimeRange.getTimeInMinutes(0, 0);

        for (TimeRange blockedTimeRange: blockedTimeRanges) {
            end = blockedTimeRange.start();
            if (end-start >= duration) {
                meetingTimeRanges.add(TimeRange.fromStartEnd(start,end,false));
            }
            if (blockedTimeRange.end() > start) {
                start = blockedTimeRange.end();
            }
        }
        // Check and add the time after the last event
        if (TimeRange.END_OF_DAY - start >= duration){
            meetingTimeRanges.add(TimeRange.fromStartEnd(start,TimeRange.END_OF_DAY,true));
        }
        
        return meetingTimeRanges;
    }
}
