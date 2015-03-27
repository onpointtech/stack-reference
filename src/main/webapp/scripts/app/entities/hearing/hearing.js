'use strict';

angular.module('aquilaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hearing', {
                parent: 'entity',
                url: '/hearing',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.hearing.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hearing/hearings.html',
                        controller: 'HearingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hearing');
                        return $translate.refresh();
                    }]
                }
            })
            .state('hearingDetail', {
                parent: 'entity',
                url: '/hearing/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aquilaApp.hearing.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hearing/hearing-detail.html',
                        controller: 'HearingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hearing');
                        return $translate.refresh();
                    }]
                }
            });
    });
