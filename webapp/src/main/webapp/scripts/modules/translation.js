angular.module('translation', ['pascalprecht.translate'])
    .config(function($translateProvider) {
        $translateProvider.translations('en', {
            SIGNIN: 'Sign In',
            PLEASE_SIGNIN: 'Please sign in',
            SIGNUP: 'Sign up',
            PLEASE_SIGNUP: 'Please sign up',
            COMPANY_NAME: 'Company name',
            EMAIL: 'Email',
            NEW_PASSWORD: 'New password',
            CONFIRM_PASSWORD: 'Confirm password',
            PASSWORD: 'Password',
            HOME: 'Home',
            POINTS: 'Points',
            POINTS_LOWER: 'points',
            PROMOTIONS: 'Promotions',
            CLIENTS: 'Clients',
            SETTINGS: 'Settings',
            LOGOUT: 'Log out',
            AWARD_POINTS: 'Award points',
            PHONE_NUMBER: 'Phone number',
            SALE_AMOUNT: 'Sale amount',
            SALE_KEY: 'Sale key',
            DESCRIPTION: 'Description',
            REQUIRED_POINTS: 'Required points',
            APPLY_PROMOTION: 'Apply promotion',
            SEARCH: 'Search',
            ADD_NEW_CLIENT: 'Add new client',
            BACK_TO_LIST: 'Back to list',
            REGISTER: 'Register',
            POINTS_CONFIGURATION: 'Points configuration',
            LOGO: 'Logo',
            THE_CLIENT_WILL_EARN: 'The client will earn',
            FOR_EVERY: 'for every',
            PURCHASE: 'Purchase',
            ADD_NEW_PROMOTION: 'Add new promotion',
            SAVE: 'Save',
            ADD_A_PROMOTION: 'Add a promotion',
            PROMOTION_DESCRIPTION: 'Promotion description',
            AN_ERROR_OCCURRED: 'An error occurred, please try again.',
            SEND_EMAIL: 'Send email',
            DELETE: 'Delete',
            DO_YOU_WANT_TO_DELETE_THE_PROMOTION: 'Do you want to delete the promotion?',
            SEND_ACTIVATION_EMAIL_AGAIN: 'Send activation email again.',
            SEND_ACTIVATION_EMAIL: 'Send activation email.',
            FORGOT_PASSWORD: 'I forgot my password',
            RECOVER_PASSWORD: 'Recover password',
            PLEASE_CHANGE_YOUR_PASSWORD: 'Please change your password',
            CHANGE_PASSWORD: 'Change password',
            YOU_HAVE_NOT_CONFIGURED_YOUR_POINTS_AWARDING_STRATEGY: 'You need to configure your points awarding strategy. ' +
            'Please go to <strong> Settings -> Points configuration</strong>  or click on <a href="/#/settings/points_configuration"> this link</a>.',
            YOU_MUST_CHANGE_YOUR_PASSWORD: 'You must change your password.',
            SEND_SMS_WITH_PROMO: 'Send SMS to install Neerpoints.'

        })
            .translations('es', {
                SIGNIN: 'Iniciar Sesión',
                PLEASE_SIGNIN: 'Inicio sesión',
                SIGNUP: 'Registrarse',
                PLEASE_SIGNUP: 'Favor de registrarse',
                COMPANY_NAME: 'Nombre de la empresa',
                EMAIL: 'Correo electrónico',
                NEW_PASSWORD: 'Nueva contraseña',
                CONFIRM_PASSWORD: 'Confirmar contraseña',
                PASSWORD: 'Contraseña',
                HOME: 'Inicio',
                POINTS: 'Puntos',
                POINTS_LOWER: 'puntos',
                PROMOTIONS: 'Promociones',
                CLIENTS: 'Clientes',
                SETTINGS: 'Configuraciones',
                LOGOUT: 'Salir',
                AWARD_POINTS: 'Otorgar puntos',
                PHONE_NUMBER: 'Número de celular',
                SALE_AMOUNT: 'Monto de la venta',
                SALE_KEY: 'No. de venta',
                DESCRIPTION: 'Descripción',
                REQUIRED_POINTS: 'Puntos necesarios',
                APPLY_PROMOTION: 'Aplicar promoción',
                SEARCH: 'Buscar',
                ADD_NEW_CLIENT: 'Agregar cliente nuevo',
                BACK_TO_LIST: 'Regresar a la lista',
                REGISTER: 'Registrar',
                POINTS_CONFIGURATION: 'Configuración de puntos',
                LOGO: 'Logotipo',
                THE_CLIENT_WILL_EARN: 'El cliente obtendrá',
                FOR_EVERY: 'por cada',
                PURCHASE: 'de compra',
                ADD_NEW_PROMOTION: 'Agregar nueva promoción',
                SAVE: 'Guardar',
                ADD_A_PROMOTION: 'Agregar promoción',
                PROMOTION_DESCRIPTION: 'Descripción de la promoción',
                AN_ERROR_OCCURRED: 'Ocurrió un error, por favor intente de nuevo.',
                SEND_EMAIL: 'Enviar email',
                DELETE: 'Eliminar',
                DO_YOU_WANT_TO_DELETE_THE_PROMOTION: '¿Desea eliminar la promoción?',
                SEND_ACTIVATION_EMAIL_AGAIN: 'Enviar correo de activación de nuevo',
                SEND_ACTIVATION_EMAIL: 'Enviar correo de activación.',
                FORGOT_PASSWORD: 'Olvidé mi contraseña',
                RECOVER_PASSWORD: 'Recuperar contraseña',
                PLEASE_CHANGE_YOUR_PASSWORD: 'Favor de cambiar su contraseña',
                CHANGE_PASSWORD: 'Cambiar contraseña',
                YOU_HAVE_NOT_CONFIGURED_YOUR_POINTS_AWARDING_STRATEGY: 'No se ha configurado la cantidad de puntos a otorgar. ' +
                'Favor de ir a <strong>Configuraciones -> Configuración de puntos</strong> o dar click en <a href="/#/settings/points_configuration"> este link</a>.',
                YOU_MUST_CHANGE_YOUR_PASSWORD: 'Es necesario que cambie su contraseña.',
                SEND_SMS_WITH_PROMO: 'Enviar SMS para instalar Neerpoints'
            });
        $translateProvider.preferredLanguage('es');
    });