'use strict';

angular.module('aquilaApp')
    .factory('Hearing', function ($resource) {
        return $resource('api/hearings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.hearingDate = new Date(data.hearingDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
