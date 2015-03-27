'use strict';

angular.module('aquilaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


