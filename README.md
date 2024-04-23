This is a beginner-friendly full-stack web application I've developed. It revolves around the CRUD actions—Create, Read, Update, and Delete—implemented with Java, MySQL, and JDBC for the backend. 

The user interface features modal dialogs created using Bootstrap, HTML, and CSS. 

However, the crux lies in connecting the backend and frontend using JavaScript, jQuery, and Ajax.

In my video "Building a FullStack Web App with JDBC, jQuery and Bootstrap: Accommodation Project" https://www.youtube.com/watch?v=s_SXX6SFRck, I delve into the challenging aspects of JS along with Bootstrap, sharing insights gained from trial and error experiences. I spoke softly during the recording, as it was nighttime, so you may need to increase the volume to ensure clear audio.

If you're a beginner, you'll likely find my tutorial easy to follow and practical.

Corrections in video:
- 2:37 User can't input roomId (auto_increment) in the form.
- 6:16 The image property of the object, not "project"
- 8:23 In the post method (addRoom() in js), the object is returned by the server thanks to RoomResource.java, not RoomDao.java.
- 9:07 In var deleteRoom, renderMore({}) is unnecessary as I have $('#roomDetailsModal').modal('hide') in the click event.

To run the project on your own machine, you may need to update ConnectionHelper.java to add your MySQL password and adjust your localhost port number accordingly.
