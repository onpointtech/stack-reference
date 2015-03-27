'use strict';

angular.module('aquilaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employer', {
                parent: 'entity',
                url: '/employer',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.employer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employer/employers.html',
                        controller: 'EmployerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employer');
                        return $translate.refresh();
                    }]
                }
            })
            .state('employerDetail', {
                parent: 'entity',
                url: '/employer/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.employer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employer/employer-detail.html',
                        controller: 'EmployerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employer');
                        return $translate.refresh();
                    }]
                }
            });
    });
