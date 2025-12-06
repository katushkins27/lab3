package models.interfaces;
import models.characters.Person;
public interface StoryTeller {
    void tellStory(String story, Person listener);
}
