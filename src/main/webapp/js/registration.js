(function ($) {

    /** SECURITY PART **/

    var accountId = null;
    var userId = null;

    /** GLOBAL FUNCTIONS **/
    /*
     * Used for initialization and also for actions  in the page.
     **/
    var displayKidData = function () {
        var reqKid = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/children/account/' + accountId,
        });
        reqKid.done(function (children) {
            $('#kidList').html(Mustache.to_html($('#kid-template').html(), children));
            //DELETE a kid
            $(".delKid").click(function () {
                var kidId = $(this).attr('data-target');
                console.log('Delete a kid:' + kidId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/children/delete/' + kidId
                });
                reqDel.done(function (e) {
                    console.log("Kid deleted");
                    displayKidData();
                });
            });
        });
    }

    var displayNurseData = function () {
        //get Nurse data.
        var reqNurse = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/nurses/account/' + accountId,
        });
        reqNurse.done(function (nurses) {
            $('#nurseList').html(Mustache.to_html($('#nurse-template').html(), nurses)); // update list nurse
            $('#nurseId').html(Mustache.to_html($('#selectNurse-template').html(), nurses)); //populate kids nurse list
            //DELETE a nurse in the list
            $(".delNurse").click(function () {
                var nurseId = $(this).attr('data-target');
                console.log('Delete a nurse:' + nurseId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/nurses/delete/' + nurseId+'/accountId/'+accountId
                });
                reqDel.done(function (e) {
                    console.log("Nurse deleted");
                    displayNurseData();
                });
            });
        });
    }

    var displayAccessListData = function () {
        var reqAccount = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/users/account/' + accountId,
        });
        reqAccount.done(function (users) {
            $('#whiteAccessList').html(Mustache.to_html($('#whiteAccesUser-template').html(), users));
            //DELETE a kid
            $(".delUser").click(function () {
                var userId = $(this).attr('data-target');
                console.log('Delete a user:' + userId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/users/delete/' + userId
                });
                reqDel.done(function (e) {
                    console.log("User deleted");
                    displayAccessListData();
                });
            });
        });
    }

    /** PAGE INITIALIZATION **/
    $("#kidBirthday").datepicker();

    $(document).ready(function () {
        $.getScript("../js/auth.js", function () {});
        var value = sessionStorage.getItem('apptoken');
        if (value != null && value != "") {
            var token = $.parseJSON(value);
            accountId = token.accountId;
            userId = token.userId;
            $('#registerTab a[href="#you"]').tab('show');
        }
        displayAccessListData();
        displayNurseData();
        displayKidData();
    });

    /** PAGE NAVIGATION **/
    $('#registerTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })
    $('#registrationStart').click(function (e) {
        $('#registerTab a[href="#you"]').tab('show');
    })
    $('#youPrevious').click(function (e) {
        $('#registerTab a[href="#what"]').tab('show');
    })
    $('#youNext').click(function (e) {
        $('#registerTab a[href="#nurse"]').tab('show');
    })
    $('#nursePrevious').click(function (e) {
        $('#registerTab a[href="#you"]').tab('show');
    })
    $('#kidsPrevious').click(function (e) {
        $('#registerTab a[href="#nurse"]').tab('show');
    })
    $('#nurseNext').click(function (e) {
        $('#registerTab a[href="#kids"]').tab('show');
    })

    /** PAGE ACTIONS **/
    $('#registerUser').click(function (e) {
        console.log("[START] registerUser " + $('#userFirstName').val());
        var mUser = {
            firstName: $('#userFirstName').val(),
            lastName: $('#userLastName').val(),
            email: $('#userEmail').val(),
            phoneNumber: $('#userPhoneNumber').val(),
            password: $('#userPassword').val(),
            type: $('#userType').val(),
            accountId: accountId,
        };

        var data = $.toJSON(mUser);
        console.log("create mUser object " + data)
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/users',
            dataType: "json",
            data: data,
        });

        req.done(function (user) {
            console.log("User created " + user);
            $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
            if (user.newUser) {
                console.log("new user created");
                var token = {
                    userId: user.userId,
                    accountId: user.accountId,
                    userName: user.firstName,
                };
                data = $.toJSON(token);
                sessionStorage.setItem('apptoken', data);
            }
            var value = sessionStorage.getItem('apptoken');
                        if (value != null && value != "") {
                            var token = $.parseJSON(value);
                            accountId = token.accountId;
                            userId = token.userId;
            }
            $.getScript("../js/auth.js");
            displayAccessListData();
        });
        console.log("[END] register user");
    })

    $('#addNurse').click(function (e) {
        var mNurse = {
            firstName: $('#nurseFirstName').val(),
            lastName: $('#nurseLastName').val(),
            phoneNumber: $('#nursePhoneNumber').val(),
            accountId: accountId,
        };
        var data = $.toJSON(mNurse);
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/nurses',
            dataType: "json",
            data: data,
        });
        req.done(function (nurse) {
            console.log("Nurse registered: " + nurse.nurseId);
            displayNurseData();
        });
    })

    $('#addKid').click(function (e) {
        var mKid = {
            firstName: $('#kidFirstName').val(),
            lastName: $('#kidLastName').val(),
            birthday: $('#kidBirthday').val(),
            nurseId: $('#nurseId').val(),
            accountId: accountId
        };
        var data = $.toJSON(mKid);
        var reqKid = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/children',
            dataType: "json",
            data: data,
        });
        reqKid.done(function (kid) {
            displayKidData();
        });
    })
})(jQuery);