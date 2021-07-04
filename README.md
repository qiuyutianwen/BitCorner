The names, email IDs, and students IDs of the members

- Ruichun Chen: ruichun.chen@sjsu.edu 015269462
- Yiyang Chen: yiyang.chen@sjsu.edu 008955622
- Runchen Tao: runchen.tao@sjsu.edu 012472161
- Yu Qiu: yu.qiu@sjsu.edu 014597791

The URL to access your app: http://54.219.138.35:3000/

Any other instructions necessary for the TA to grade the app

- In the Dashboard, the lap, lbp, and ltp needs sometime for the initial loading.
- For the send bill feature, user needs to search for a user's nickname via the search bar, then click on the user's nickname for sending a bill.
- For some important operations, there might be a short delay, because the synchrnous call to send out email to the user

Build instructions
Frontend:

- run `npm install`
- run `npm start`

Backend:

- install Java, Maven
- go to folder where pom.xml is located
- run `mvn clean install`
- run `java -jar bitCorner-0.0.1-SNAPSHOT.jar`
