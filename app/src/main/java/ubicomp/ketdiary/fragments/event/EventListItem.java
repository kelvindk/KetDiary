package ubicomp.ketdiary.fragments.event;

/**
 * An object of this class represents all comtains of an item in event list.
 *
 * Created by kelvindk on 16/6/7.
 */
public class EventListItem {

    // The icons of eight event category in R.drawable
    private int eventCategory;

    // The string of event date.
    private String eventDate = null;

    // 0~5 skull to show dangerous level of this event
    private int eventDangerousLevel;

    // String for event description
    String eventDescription = null;

    public EventListItem() {

    }


}
