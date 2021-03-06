package com.google.sps.Objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public final class Post {

  private final long postId;
  private final String authorId;
  private final String authorName;
  private final String authorPic;
  private final String postText;
  private final ArrayList<Comment> comments;
  private final String challengeName;
  private final long timestamp;
  private final String img;
  private final HashSet<String> likes;
  
  public Post(
    long postId, 
    String authorId, 
    String authorName,
    String authorPic,
    String postText, 
    ArrayList<Comment> comments, 
    String challengeName, 
    long timestamp, 
    String img, 
    HashSet<String> likes
  ) {
    this.postId = postId;
    this.timestamp = timestamp;
    this.postText = postText;
    this.authorId = authorId;
    this.authorName = authorName;
    this.authorPic = authorPic;
    this.comments = comments;
    this.challengeName = challengeName;
    this.img = img;
    this.likes = likes;
  }

  public static Post fromEntity(Entity entity) throws IOException {
    long postId = entity.getKey().getId();
    long timestamp = (long) entity.getProperty("timestamp");
    String authorId = (String) entity.getProperty("authorId");
    String authorName = (String) entity.getProperty("authorName");
    String postText = (String) entity.getProperty("postText");
    String challengeName = (String) entity.getProperty("challengeName");
    String img = (String) entity.getProperty("img");
    HashSet<String> likes = (entity.getProperty("likes") == null) 
      ? new HashSet<>() 
      : new HashSet<String>((ArrayList<String>) entity.getProperty("likes"));   
    ArrayList<Comment> comments = new ArrayList<>();
     if (entity.getProperty("comments") != null) {
      createCommentObjectList(comments, entity);
    }
    // Get author profile pic from authorId 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String authorPic;
    try {
      Entity userEntity = datastore.get(KeyFactory.createKey("User", authorId));
      authorPic = (String) userEntity.getProperty("profilePic");
    } catch (EntityNotFoundException e) {
      authorPic = null;
    }
    return new Post(postId, authorId, authorName, authorPic, postText, comments, challengeName, timestamp, img, likes);
  }

  private static void createCommentObjectList(ArrayList<Comment> comments, Entity entity) {
    ArrayList<EmbeddedEntity> commentEntitys = (ArrayList<EmbeddedEntity>) entity.getProperty("comments");
    for (EmbeddedEntity comment: commentEntitys) {
      comments.add(Comment.fromEntity(comment));
    }
  }

  public Entity toEntity() {
    Entity entity = new Entity("Post");
    entity.setProperty("authorId", this.authorId);
    entity.setProperty("authorName", this.authorName);
    entity.setProperty("timestamp", this.timestamp);
    entity.setProperty("postText", this.postText);
    entity.setProperty("challengeName", this.challengeName);
    entity.setProperty("img", this.img);
    entity.setProperty("likes", new ArrayList<>(this.likes));
    entity.setProperty("comments", Comment.createCommentEntities(this.comments));
    return entity;
  }

  @Override 
  public boolean equals(Object other) {
    if (other == null) return false;
    if (other == this) return true;
    if (!(other instanceof Post)) return false;
    Post post = (Post) other;
    return timestamp == post.timestamp &&
      authorId.equals(post.authorId) &&
      authorName.equals(post.authorName) &&
      authorPic.equals(post.authorPic) &&
      postText.equals(post.postText) &&
      challengeName.equals(post.challengeName) &&
      img.equals(post.img) &&
      likes.containsAll(post.likes) &&
      comments.containsAll(post.comments);
  }

  public long getPostId() {
    return postId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getPostText() {
    return postText;
  }

  public String getAuthorPic() {
    return authorPic;
  }

  public String getAuthorId() {
    return authorId;
  }

  public String getAuthorName() {
    return authorName;
  }

  public ArrayList<Comment> getComments() {
    return comments;
  }

  public String getChallengeName() {
    return challengeName;
  }

  public String getImg() {
    return img;
  }

  public HashSet<String> getLikes() {
    return likes;
  }

  public void addComment(Comment newComment) {
    this.comments.add(newComment);
  }

  public void addLike(String userId) {
    this.likes.add(userId);
  }

  public void removeLike(String userId) {
    this.likes.remove(userId);
  }
}
