'use strict';

angular.module('aquilaApp')
    .controller('EmployerController', function ($scope, Employer, ParseLinks) {
        $scope.employers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Employer.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.employers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Employer.update($scope.employer,
                function () {
                    $scope.loadAll();
                    $('#saveEmployerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Employer.get({id: id}, function(result) {
                $scope.employer = result;
                $('#saveEmployerModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Employer.get({id: id}, function(result) {
                $scope.employer = result;
                $('#deleteEmployerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Employer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEmployerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.employer = {name: null, address: null, contactName: null, contactPhone: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
