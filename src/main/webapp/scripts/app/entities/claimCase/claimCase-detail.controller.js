'use strict';

angular.module('aquilaApp')
    .controller('ClaimCaseDetailController', function ($scope, $stateParams, ClaimCase, Employer) {
        $scope.claimCase = {};
        $scope.load = function (id) {
            ClaimCase.get({id: id}, function(result) {
              $scope.claimCase = result;
            });
        };
        $scope.load($stateParams.id);
    });
