package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasOne;
import com.amplifyframework.core.model.annotations.BelongsTo;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
@Index(name = "byTask", fields = {"taskID"})
public final class Task implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField STATE_ID = field("stateID");
  public static final QueryField TITLE = field("title");
  public static final QueryField BODY = field("body");
  public static final QueryField FILEKEY = field("filekey");
  public static final QueryField TEAM = field("taskID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String stateID;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="String") String filekey;
  private final @ModelField(targetType="State") @HasOne(associatedWith = "id", type = State.class) State state = null;
  private final @ModelField(targetType="Team") @BelongsTo(targetName = "taskID", type = Team.class) Team team;
  public String getId() {
      return id;
  }
  
  public String getStateId() {
      return stateID;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }

  public String getFilekey() {
      return filekey;
  }

  public State getState() {
      return state;
  }
  
  public Team getTeam() {
      return team;
  }
  
  private Task(String id, String stateID, String title, String body, String filekey, Team team) {
    this.id = id;
    this.stateID = stateID;
    this.title = title;
    this.body = body;
    this.filekey = filekey;
    this.team = team;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getStateId(), task.getStateId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getFilekey(), task.getFilekey()) &&
              ObjectsCompat.equals(getTeam(), task.getTeam());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getStateId())
      .append(getTitle())
      .append(getBody())
      .append(getFilekey())
      .append(getTeam())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("stateID=" + String.valueOf(getStateId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("filekey=" + String.valueOf(getFilekey()) + ", ")
      .append("team=" + String.valueOf(getTeam()))
      .append("}")
      .toString();
  }
  
  public static StateIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Task justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Task(
      id,
      null,
      null,
      null,
      null,
            null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      stateID,
      title,
      body,
      filekey,
      team);
  }
  public interface StateIdStep {
    TitleStep stateId(String stateId);
  }
  

  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep body(String body);
    BuildStep filekey(String filekey);
    BuildStep team(Team team);
  }
  

  public static class Builder implements StateIdStep, TitleStep, BuildStep {
    private String id;
    private String stateID;
    private String title;
    private String body;
    private String filekey;
    private Team team;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          stateID,
          title,
          body,
          filekey,
          team);
    }
    
    @Override
     public TitleStep stateId(String stateId) {
        Objects.requireNonNull(stateId);
        this.stateID = stateId;
        return this;
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }

      @Override
      public BuildStep filekey(String filekey) {
          this.filekey = filekey;
          return this;
      }
    
    @Override
     public BuildStep team(Team team) {
        this.team = team;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String stateId, String title, String body, String filekey, Team team) {
      super.id(id);
      super.stateId(stateId)
        .title(title)
        .body(body)
        .filekey(filekey)
        .team(team);
    }
    
    @Override
     public CopyOfBuilder stateId(String stateId) {
      return (CopyOfBuilder) super.stateId(stateId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }

      @Override
      public CopyOfBuilder filekey(String filekey) {
          return (CopyOfBuilder) super.filekey(filekey);
      }
    
    @Override
     public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
  }
  
}
