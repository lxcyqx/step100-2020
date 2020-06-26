function init() {
  loadPosts();
  uploadImage();
  //fetchBlobstoreUrlAndShowForm();
}

function uploadImage() {
  console.log("in upload image");
  document.getElementById('camera-btn').addEventListener('click', openDialog);
    function openDialog() {
      console.log("in open dialogue");
        document.getElementById('fileid').click();
        document.getElementById('fileid').addEventListener('change', fetchBlobstoreUrlAndShowForm);

        function fetchBlobstoreUrlAndShowForm() {
          console.log("in fetch");
          fetch('/post-image-handler')
          .then((response) => {
            return response.text();
          })
          .then((imageUploadUrl) => {
            const messageForm = document.getElementById('post-form');
            messageForm.action = imageUploadUrl;
            console.log(imageUploadUrl);
          });
        }
    }
}

function loadPosts() {
  fetch('/group-post').then(response => response.json()).then((posts) => {
    const allPostsList = document.getElementById('posts-container');
    allPostsList.innerHTML = '';
    for (var i = 0; i < posts.length; i++) {
      allPostsList.appendChild(createSinglePost(posts[i]));
    }
  });
}

function createSinglePost(post) {
  const postDiv = document.createElement('div');
  postDiv.className = "post-div";
  postDiv.appendChild(createProfileImg(post));
  postDiv.append(createAuthor(post));
  postDiv.append(createPostText(post));
  return postDiv;
}

// Create HTML element for post profile img
function createProfileImg() {
   const profileImgDiv = document.createElement('div');
  profileImgDiv.className = "post-img align-vertical";
  return profileImgDiv;
}

// Create HTML element for post author name 
function createAuthor(post) {
  const postAuthor = document.createElement('h3');
  postAuthor.className = "post-author align-vertical";
  postAuthor.innerText = post.authorId;
  return postAuthor;
}

// Create HTML element for post text
function createPostText(post) {
  const postContent = document.createElement('p');
  postContent.className = "post-content";
  postContent.innerText = post.postText;
  return postContent;
}

// Gets URL for uploaded image
function fetchBlobstoreUrlAndShowForm() {
  console.log("in fetch");
  fetch('/post-image-handler')
  .then((response) => {
  	return response.text();
  })
  .then((imageUploadUrl) => {
    const messageForm = document.getElementById('post-form');
    messageForm.action = imageUploadUrl;
    console.log(imageUploadUrl);
  });
}