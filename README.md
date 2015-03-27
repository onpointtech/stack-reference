README for aquila
==========================
#Setup
Follow the [Local Installation](http://jhipster.github.io/installation.html) instructions on the JHipster site. You probably have the first couple, but you'll need to install Node and its package manager (NPM) to begin with. Once you have that, NPM can install yeoman, bower, and grunt.

After that, open a cmd and run `mvn spring-boot:run` from the root directory. The first run will probably take 20 minutes to get everything. Maven is actually calling `npm install` and `bower install` and downloading all the nested repos. Very similar to maven itself. Once it finishes, you'll have a spring mvc app (and more importantly a web api) hosted at localhost:8080.

Open another cmd and run `grunt serve` from the root directory. After a minute for the first load, you'll be able to hit the angular version of the site at localhost:3000.

Play around with the admin section. That's the coolest part.