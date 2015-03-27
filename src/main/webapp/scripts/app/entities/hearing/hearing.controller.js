'use strict';

angular.module('aquilaApp')
    .controller('HearingController', function ($scope, Hearing, ClaimCase, User, ParseLinks) {
        $scope.hearings = [];
        $scope.claimcases = ClaimCase.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Hearing.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.hearings.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.hearings = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Hearing.update($scope.hearing,
                function () {
                    $scope.reset();
                    $('#saveHearingModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Hearing.get({id: id}, function(result) {
                $scope.hearing = result;
                $('#saveHearingModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Hearing.get({id: id}, function(result) {
                $scope.hearing = result;
                $('#deleteHearingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Hearing.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteHearingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.hearing = {hearingDate: null, location: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
