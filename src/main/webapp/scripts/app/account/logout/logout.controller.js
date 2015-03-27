'use strict';

angular.module('aquilaApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
