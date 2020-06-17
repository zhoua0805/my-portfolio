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

        // Check for invalid input of duration.
        if(duration > TimeRange.WHOLE_DAY.duration()) {
            return Arrays.asList();
        }

        // First get the time ranges that work for all attendees.
        // Don't optimize this because optimization should only apply to
        // mandatory attendees 
        int[] meetingStatesWithAllAttendees = 
            getMinutesState(events, request.getAllAttendees());
        Collection<TimeRange> meetingTimeRangesWithAllAttendees = 
            convertStateToTimeRanges(meetingStatesWithAllAttendees, duration, request.getAllAttendees().size(), false);

        // If there are no time ranges that work for all attendees, get the optimized time
        // ranges that work for best for the mandatory attendees.
        // If there are no mandatory attendees, treat the optional attendees as mandatory attendees
        // and optimize the timeranges
        if (request.getAttendees().size() == 0){
            Collection<TimeRange> meetingTimeRangesWithOptionalAttendees = 
                convertStateToTimeRanges(meetingStatesWithAllAttendees, duration, request.getAllAttendees().size(), true);
            return meetingTimeRangesWithOptionalAttendees;
        }else if (meetingTimeRangesWithAllAttendees.size() == 0) {
            int[] meetingStatesWithMandatoryAttendees = 
                getMinutesState(events, request.getAttendees());
            Collection<TimeRange> meetingTimeRangesWithMandatoryAttendees = 
                convertStateToTimeRanges(meetingStatesWithMandatoryAttendees, duration, request.getAttendees().size(), true);
            return meetingTimeRangesWithMandatoryAttendees;
        }else{
            return meetingTimeRangesWithAllAttendees;
        }
    }

    // Get all the valid event time ranges and sort them by the start time. 
    // Only add to the list if at least one attendee from the event is 
    // a required attendee in the meeting request. (Otherwise, the event is irrelevant.)
    public List<TimeRange> getValidEventTimeRanges(Collection<Event> events, Collection<String> requestAttendees) {
        List<TimeRange> eventTimeRanges = events.stream().filter(
            event -> event.getAttendees().stream().anyMatch(
            attendee -> requestAttendees.contains(attendee)))
            .map(e -> e.getWhen())
            .collect(Collectors.toList());
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);
        return eventTimeRanges;
    }

    // Function that returns all valid time ranges based on a list of event time ranges
    // Approach 1: start form start of day, and add available timeranges as we traverse through the events' timeranges
    public Collection<TimeRange> getMeetingTimeRanges(List<TimeRange> eventTimeRanges, long duration) {
        Collection<TimeRange> meetingTimeRanges = new ArrayList();
        int start = TimeRange.getTimeInMinutes(0, 0);
        int end = TimeRange.getTimeInMinutes(0, 0);

        for (TimeRange eventTimeRange: eventTimeRanges) {
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

    // Approach 2: assign numbers to every minute in the day representing their state
    // 0 means not occupied, -1 means 1 person is not available at that minute, etc.
    public int[] getMinutesState(Collection<Event> events, Collection<String> requestAttendees) {
        int[] minutesState = new int[24 * 60]; 
        for (Event event: events) {
            for (int i = event.getWhen().start(); i < event.getWhen().end(); i++) {
                minutesState[i] -= getNumOfAttendees(event, requestAttendees);
            }
        }
        return minutesState;
    }

    // Convert the array of states to a list of valid time ranges. If optimize is false, must return timeranges 
    // that work for everyone (or an empty list if none); if optimize is true, return the best solution.
    public Collection<TimeRange> convertStateToTimeRanges(int[] minutesState, 
                long duration, long numOfAttendees, boolean optimize) {
        Collection<TimeRange> meetingTimeRanges = Arrays.asList();
        int max = Arrays.stream(minutesState).max().getAsInt(); 
        int min = Arrays.stream(minutesState).min().getAsInt();
        int j = 0;

        if (!optimize){
            return getTimeRangesBasedOnState(minutesState, duration, 0);
        }

        // The valid timeranges are sequences with consecutive best-state's.
        for (j = max; j >= min; j--) {
            // If we end up having to search the worst possible state, this means no one 
            // is actually availble, so just return an empty list.
            if (Math.abs(j) == numOfAttendees && numOfAttendees != 0) {
                return Arrays.asList();
            }

            meetingTimeRanges = 
                    getTimeRangesBasedOnState(minutesState, duration, j);

            // Include the next state to search if no time ranges are found
            // Otherwise, break
            if (meetingTimeRanges.size() != 0) {
                break;
            }
        }

        System.out.printf("An optimization is found with %d person(s) unavailable. %n", Math.abs(j));
        return meetingTimeRanges;
    }

    public Collection<TimeRange> getTimeRangesBasedOnState(int[] minutesState, long duration, int state) {
        Collection<TimeRange> meetingTimeRanges = new ArrayList();
        int prestart = TimeRange.getTimeInMinutes(0, 0) - 1;
        int count = 0;
        for (int i = 0; i < minutesState.length; i++) {
            int currentState = minutesState[i];
            if (currentState >= state){
                count += 1;
            }else{
                if (count >= duration) {
                    meetingTimeRanges.add(TimeRange. fromStartDuration(prestart+1, count));
                }
                prestart = i;
                count = 0;
            }
        }
        // Add the last time range if applicable.
        if (TimeRange.END_OF_DAY - prestart  >= duration) {
            meetingTimeRanges.add(TimeRange. fromStartEnd(prestart+1, TimeRange.END_OF_DAY, true));
        }
        return meetingTimeRanges;
    }

    // Get the number of relevant attendees in an event
    public long getNumOfAttendees(Event event, Collection<String> requestAttendees) {
        long numOfAttendees = event.getAttendees().stream().filter(
            attendee -> requestAttendees.contains(attendee))
            .count();
        return numOfAttendees;
    }
}
