package com.google.sps.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.Objects.Post;
import com.google.sps.Objects.Comment;

/**
 * Unit tests for Post.
 *
 */
public class PostTest {

  private static final String AUTHOR_ID = "123123123";
  private static final String POST_TEXT = "a great post";
  private static final String CHALLENGE_NAME = "run 4 miles";
  private static final String IMG = "";
  private static final long TIMESTAMP = 123123123;

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig()
              .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

  private Post post;

  @Before
  public void setUp() {
    helper.setUp();
    post = 
      new Post(
        4324344, /* postId */ 
        AUTHOR_ID, /* authorId */ 
        POST_TEXT, /* postText */
        new ArrayList<Comment>(), /* comments */
        CHALLENGE_NAME, /* challengeName */
        TIMESTAMP, /* timestamp */
        IMG, /* img */
        new HashSet<String>() /* likes */);
  }

  @After
  public void tearDown() {
    helper.tearDown();
    post = null;
  }

  @Test
  public void getPostTextTest() {
    assertEquals(post.getPostText(), POST_TEXT);
  }

  @Test 
  public void getImgtTest() {
    assertEquals(post.getImg(), IMG);
  }

  @Test
  public void getChallengeNameTest() {
    assertEquals(post.getChallengeName(), CHALLENGE_NAME);
  }

  @Test 
  public void getCommentsTest() {
    assertEquals(post.getComments().size(), 0);
  }

  @Test
  public void getLikesTest() {
    assertEquals(post.getLikes().size(), 0);
    post.addLike("user 1");
    post.addLike("user 2");
    HashSet<String> testLikes = new HashSet<String>();
    testLikes.add("user 1");
    testLikes.add("user 2");
    
    assert testLikes.containsAll(post.getLikes());
  }

  @Test
  public void addCommentTest() {
    Comment testComment1 =  
      new Comment(
        4324344, /* userId */ 
        "a great comment", /* commentText */ 
        "123123123" /* userId */ );
    Comment testComment2 = 
      new Comment(
        55555555, /* userId */ 
        "another great comment", /* commentText */ 
        "09090909" /* userId */ );
    post.addComment(testComment1);
    post.addComment(testComment2);

    assert post.getComments().size() == 2;
    assertTrue(post.getComments().get(0).equals(testComment1));
    assertTrue(post.getComments().get(1).equals(testComment2));
  }

  @Test
  public void addLikeTest() {
    post.addLike("user 1");
    post.addLike("user 2");
    HashSet<String> likes = post.getLikes();

    assert likes.size() == 2;
    assert likes.contains("user 1");
    assert likes.contains("user 2");
  }

  @Test
  public void removeLikeTest() {
    post.addLike("test user 1");
    post.addLike("test user 2");
    HashSet<String> likes = post.getLikes();
    assert likes.size() == 2;

    post.removeLike("test user 1");
    likes = post.getLikes();
    assert likes.size() == 1;
    assert likes.contains("test user 2");

    post.removeLike("test user 2");
    likes = post.getLikes();
    assert likes.size() == 0;
  }

  @Test
  public void getAndCreatePostEntityTest() {
    Entity entity = post.createPostEntity();
    Post returnedPost = Post.getPostEntity(entity);

    assertTrue(returnedPost.equals(post));
  }
}