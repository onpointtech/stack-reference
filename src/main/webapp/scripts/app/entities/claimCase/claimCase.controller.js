'use strict';

angular.module('aquilaApp')
    .controller('ClaimCaseController', function ($scope, ClaimCase, Employer, ParseLinks) {
        $scope.claimCases = [];
        $scope.employers = Employer.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ClaimCase.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.claimCases = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            ClaimCase.update($scope.claimCase,
                function () {
                    $scope.loadAll();
                    $('#saveClaimCaseModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            ClaimCase.get({id: id}, function(result) {
                $scope.claimCase = result;
                $('#saveClaimCaseModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            ClaimCase.get({id: id}, function(result) {
                $scope.claimCase = result;
                $('#deleteClaimCaseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ClaimCase.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClaimCaseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.claimCase = {legacyCaseNumber: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
