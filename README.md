# pillbug

# Post-hackathon update #1
The mobile application has been redesigned using material design. Here are some snapshots!  

<p float="left">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/login.png" width="150px">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/weeklyview.png" width="150px">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/weeklyviewdata.png" width="150px">
</p>

<p float="left">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/navigation.png" width="150px">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/calendarview.png" width="150px">
<img src="https://github.com/anthonyc1/pillbug/blob/master/screenshots/calendarviewdata.png" width="150px">
</p>

# Inspiration
When brainstorming for hackathon ideas, we all wanted to create a product or service that would be helpful to many people. While there are countless hacks aimed towards millennials, we wanted to take this hackathon as an opportunity to create a useful hack for the elderly. One of our team members recalled a scary incident where his grandfather overdosed on medication after forgetting that he already took the medicine. The grandfather was taken to the ER as a result. Thankfully, while no permanent damage happened to grandpa, his grandson really want to create a product to prevent this from happening again. Meet - the smart pillbox.

# What it does
The pillbox keeps caretakers up-to-date with the status of their patient's pillbox. The pillbox would monitor the pillbox on a minute-by-minute basis to check for anomalies. The pillbox talks to a backend server which talks to a mobile app that is easily accessible by caretakers. If the pillbox detects anything that is off, the mobile app would notify the caretaker so immediate action can be taken. This allows caretakers to ensure that their patient don't forget to take their pills or don't overdose on any pills.

# How we built it
To check the status of a pillbox, a raspberry pi and a camera is positioned over the 2x7 pillbox via a stand and captures images of said pillbox. We knew that in real-life situations the amount of lighting (and thus shadows) over the box can be arbitrary and less than ideal, so we accounted for that to make our computer vision model robust. Through k-means clustering algorithm, we were able to identify which buckets of the pillbox had pills or were empty. This information is then sent to our backend hosted on MongoDB Stitch. After some processing, we utilize SparkPost to inform caretakers via text or email (if requested) and then we update the virtual view of the pillbox in our Android mobile application. Via our mobile app, caretakers would have the real-time status of the pillbox and can also access the medicine intake history of said patient.

# Challenges we ran into
Getting MongoDB Stitch and Sparkpost to work. Developing the computer vision model to recognize the pills inside the cells. Taking the UI to the next level. Working against the clock.

# Accomplishments that we're proud of
We were able to split our strengths and bring together many different technologies for this project. Two of our team members worked with opencv and hardware to get the computer vision model working and robust. Our third member developed the backend and APIs, and our last member developed the Android mobile app.

# What we learned
Focus on developing the MVP given the time constraints. Bringing your own hardware can be super helpful for a hackware hack.

# What's next for PillBug: The Smarter PillBox
What's next for PillBug is a rotatable camera that to capture images of nearby surrounding. We also want to add LED lights to the pillbox to better interact with our patients.

# Check us out on Devpost!
https://devpost.com/software/pillbug-nfxm54