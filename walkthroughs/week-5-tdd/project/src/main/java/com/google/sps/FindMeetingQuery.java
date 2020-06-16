// Copyright 2019 Google LLC
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

public final class FindMeetingQuery {
    //Approach: start form start of day, and add available timeranges as we traverse through the events' timeranges
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        long duration = request.getDuration();

        // Check for invalid input of duration.
        if(duration > TimeRange.WHOLE_DAY.duration()) {
            return Arrays.asList();
        }

        // First get the time ranges that work for all attendees. 
        List<TimeRange> eventTimeRangesWithAllAttendees = 
            getValidEventTimeRanges(events, request.getAllAttendees());
        Collection<TimeRange> meetingTimeRangesWithAllAttendees = 
            getMeetingTimeRanges(eventTimeRangesWithAllAttendees, duration);

        // If there are no time ranges that work for all attendees, get the time
        // ranges that work for only the mandatory attendees.
        if (meetingTimeRangesWithAllAttendees.size() == 0) {
            List<TimeRange> eventTimeRangesWithMandatoryAttendees = 
                getValidEventTimeRanges(events, request.getAttendees());
            Collection<TimeRange> meetingTimeRangesWithMandatoryAttendees = 
                getMeetingTimeRanges(eventTimeRangesWithMandatoryAttendees, duration);
            return meetingTimeRangesWithMandatoryAttendees;
        }else{
            return meetingTimeRangesWithAllAttendees;
        }
    }

    // Get all the valid event time ranges and sort them by the start time. 
    // Only add to the list if at least one attendee from the event is 
    // a required attendee in the meeting request. (Otherwise, the event is irrelevant.)
    public List<TimeRange> getValidEventTimeRanges(Collection<Event> events, Collection<String> attendees) {
        List<TimeRange> eventTimeRanges = new ArrayList();
        for (Event event: events) {
            for (String attendee: event.getAttendees()){
                if (attendees.contains(attendee)){
                    eventTimeRanges.add(event.getWhen());
                    break;
                }
            }
        }
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);
        return eventTimeRanges;
    }

    public Collection<TimeRange> getMeetingTimeRanges(List<TimeRange> eventTimeRanges, long duration) {
        Collection<TimeRange> meetingTimeRanges = new ArrayList();
        int start = 0;
        int end = 0;

        // Loop through all events.
        for (int i = 0; i < eventTimeRanges.size(); i++) {
            TimeRange eventTimeRange = eventTimeRanges.get(i);
            end = eventTimeRange.start();
            if (end-start >= duration) {
                meetingTimeRanges.add(TimeRange.fromStartEnd(start,end,false));
            }
            if (eventTimeRange.end() > start) {
                start = eventTimeRange.end();
            }
        }
        // Add the last event if applicable.
        if (TimeRange.END_OF_DAY - start >= duration){
            meetingTimeRanges.add(TimeRange.fromStartEnd(start,TimeRange.END_OF_DAY,true));
        }
        
        return meetingTimeRanges;
    }
}
