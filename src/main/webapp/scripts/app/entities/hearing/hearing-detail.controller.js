'use strict';

angular.module('aquilaApp')
    .controller('HearingDetailController', function ($scope, $stateParams, Hearing, ClaimCase, User) {
        $scope.hearing = {};
        $scope.load = function (id) {
            Hearing.get({id: id}, function(result) {
              $scope.hearing = result;
            });
        };
        $scope.load($stateParams.id);
    });
