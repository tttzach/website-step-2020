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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FindMeetingQuery {

  // Get all suitable time ranges which do not conflict with the events of attendees in the meeting request
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long proposedDuration = request.getDuration();
    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();

    List<TimeRange> mandatoryUnavailableTimeRanges = getUnavailableTimeRanges(events, mandatoryAttendees);
    List<TimeRange> optionalUnavailableTimeRanges = getUnavailableTimeRanges(events, optionalAttendees);
    List<TimeRange> mandatoryAvailableTimeRanges = getAvailableTimeRanges(mandatoryUnavailableTimeRanges, proposedDuration);
    List<TimeRange> combinedUnavailableTimeRanges = Stream.concat(mandatoryUnavailableTimeRanges.stream(), optionalUnavailableTimeRanges.stream()).collect(Collectors.toList());
    List<TimeRange> combinedAvailableTimeRanges = getAvailableTimeRanges(combinedUnavailableTimeRanges, proposedDuration);

    if (combinedAvailableTimeRanges.isEmpty() && !mandatoryAttendees.isEmpty()) {
      return mandatoryAvailableTimeRanges;
    }
    return combinedAvailableTimeRanges;
  }

  // Collect all time ranges of events that involve any of the attendees
  private List<TimeRange> getUnavailableTimeRanges(Collection<Event> events, Collection<String> attendees) {
    List<TimeRange> eventTimeRanges = new ArrayList<>();
    for (Event event : events) {
      Set<String> eventAttendees = event.getAttendees();
      TimeRange eventTimeRange = event.getWhen();
      for (String eventAttendee : eventAttendees) {
        if (attendees.contains(eventAttendee)) {
          eventTimeRanges.add(eventTimeRange);
        }
      }
    }
    return eventTimeRanges;
  }

  private List<TimeRange> getAvailableTimeRanges(List<TimeRange> eventTimeRanges, long proposedDuration) {
    List<TimeRange> sortedEventStartTimeRanges  = new ArrayList<>(eventTimeRanges);
    Collections.sort(sortedEventStartTimeRanges, TimeRange.ORDER_BY_START);

    int proposedStartTime = TimeRange.START_OF_DAY;
    List<TimeRange> proposedTimeRanges = new ArrayList<>();

    for (TimeRange timeRange : sortedEventStartTimeRanges) {
      if (timeRange.start() > proposedStartTime) {
        TimeRange newTimeRange = TimeRange.fromStartEnd(proposedStartTime, timeRange.start(), false);
        if (newTimeRange.duration() >= proposedDuration) {
          proposedTimeRanges.add(newTimeRange);
        }
        proposedStartTime = timeRange.end();
      } else {
        if (proposedStartTime < timeRange.end()) {
          proposedStartTime = timeRange.end();
        }
      }
    }

    // Add available time slot from end of last event to end of day, if applicable
    if ((TimeRange.END_OF_DAY - proposedStartTime) >= proposedDuration) {
      proposedTimeRanges.add(TimeRange.fromStartEnd(proposedStartTime, TimeRange.END_OF_DAY, true));
    }

    return proposedTimeRanges;
  }

}
