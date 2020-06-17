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
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.*;

public final class FindMeetingQuery {
    //Approach: start form start of day, and add available timeranges as we traverse through the events' timeranges
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Collection<TimeRange> validTimeRanges = new ArrayList();
        long duration = request.getDuration();
        int start = 0;
        int end = 0;
        int lastEnd = 0;

        // Get all the event time ranges and sort them by the start time. 
        // Only add to the list if at least one attendee from the event is 
        //a required attendee in the meeting request. (Otherwise, the event is irrelevant.)
        List<TimeRange> eventTimeRanges = events.stream().filter(
         event -> event.getAttendees().stream().anyMatch(
             attendee -> request.getAttendees().contains(attendee)))
         .map(e -> e.getWhen())
         .collect(Collectors.toList());
        Collections.sort(eventTimeRanges, TimeRange.ORDER_BY_START);
        
        // Check for invalid input of duration.
        if(duration > TimeRange.WHOLE_DAY.duration()) {
            return validTimeRanges;
        }
        // If there are no scheduled events, the meeting can be scheduled at any time. 
        if (eventTimeRanges.size() == 0){
            validTimeRanges.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,TimeRange.END_OF_DAY,true));
        }else {
            for (TimeRange eventTimeRange: eventTimeRanges) {
                end = eventTimeRange.start();
                if (end-start >= duration) {
                    validTimeRanges.add(TimeRange.fromStartEnd(start,end,false));
                }
                start = eventTimeRange.end();
                if (eventTimeRange.end() > lastEnd) {
                    lastEnd = eventTimeRange.end();
                }
            }
            // Add the last event if applicable.
            if (TimeRange.END_OF_DAY - lastEnd >= duration){
                validTimeRanges.add(TimeRange.fromStartEnd(lastEnd,TimeRange.END_OF_DAY,true));
            }
        }
        return validTimeRanges;
    }
}
