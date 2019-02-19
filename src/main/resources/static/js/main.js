var messageApi = Vue.resource('/v1/messages{/id}{?roomId}');
var roomApi = Vue.resource('/v2/room{/roomId}');


Vue.component('message-form', {
    props: ['messages'],
    data: function () {
        return {
            text: '',

        }
    },

    template: '<div class="form-group" >' +
        '<input type="text" placeholder="Text of message" v-model="text"/>' +
        '<input type="button" :class="{button:true}" value="Save" @click="save" />' +
        '' +
        '</div>',
    methods: {
        save: function () {
            var message = {text: this.text};

            messageApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.messages.push(data);

                })
            )
        }
    }
});

Vue.component('modal', {
    template: '<script type="text/x-template" id="modal-template">\n' +
        '  <transition name="modal">\n' +
        '    <div class="modal-mask">\n' +
        '      <div class="modal-wrapper">\n' +
        '        <div class="modal-container">\n' +
        '\n' +
        '          <div class="modal-header">\n' +
        '            <slot name="header">\n' +
        '              default header\n' +
        '            </slot>\n' +
        '          </div>\n' +
        '\n' +
        '          <div class="modal-body">\n' +
        '            <slot name="body">\n' +
        '              default body\n' +
        '            </slot>\n' +
        '          </div>\n' +
        '\n' +
        '          <div class="modal-footer">\n' +
        '            <slot name="footer">\n' +
        '              default footer\n' +
        '              <button class="modal-default-button" @click="$emit(\'close\')">\n' +
        '                OK\n' +
        '              </button>\n' +
        '            </slot>\n' +
        '          </div>\n' +
        '        </div>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </transition>\n' +
        '</script>'
})

Vue.component('user', {
    props: ['user'],
    template: '<div>' +
        '<li class="list-group-item list-group-item-action"\n' +
        '                            v-on:click="selectUser(user.name)">' +
        ' {{user.name}}' +
        '</li>' +
        '</div>',
    methods: {
        selectUser: function (name) {

            var room = {
                name: 'private',
                participantsName: [name]
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    console.log(data);
                    if(!frontendData.rooms.includes(data)) {
                        frontendData.rooms.push(data);
                    }

                    frontendData.currentRoomId = data.id;

                    //todo: extract to function
                    while(frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function(item) {
                        frontendData.messages.push(item);
                    });
                })
            )

        }
    }

});

Vue.component('usersList', {
    props: ['users'],
    template:' <div class="card-body">' +
        '  <ul class="list-group">' +
        ' <user v-for="user in users" :key="user.id" :user="user"/>' +
        '</ul> </div>'
});

Vue.component('message-row', {
    props: ['message', 'profile'],
    template: '<div> ' +

        '<li class="list-group-item list-group-item-action mb-2 block1">' +
        '<div class="list-group">' +
        '  <div>{{message.text}}</div>' +
        '  <div class="author"><i>{{message.authorName}}</i>' +
        '</div></div>'+
        ' </li></div>' +

        '</div>'
});

Vue.component('inputForm', {
    props:['messages'],
    data: function () {
        return {
            text: '',
        }
    },
    template: '<div class="card-header">\n' +
        '                <div class="form-group">\n' +
        '                    <form class="was-validated">\n' +
        '                        <div class="mb-3">\n' +
        '                            <label for="validationTextarea">Input message:</label>\n' +
        '                            <textarea v-model="text" class="form-control is-invalid"\n' +
        '                                      id="validationTextarea" placeholder="Required example textarea"\n' +
        '                                      required></textarea>\n' +

        '                        </div>\n' +
        '                    </form>\n' +

        '                    <input type="button" class="btn btn-lg btn-primary mb-2 button" value="Save" @click="save(text)"/>\n' +
        '                </div>\n' +
        '            </div>',
    methods: {

        save: function (text) {
            var message = {text: text, roomId:frontendData.currentRoomId};
            messageApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.messages.push(data);
                })
            )
        }
    }
});

Vue.component('messages-list', {
    props: ['messages'],
    template: '<div class="tableChat ">' +
        '<message-form :messages="messages" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</div>',


});
Vue.component('rightPanel', {
    props: ['messages', 'profile'],
    template: '<div>' +
        ' <inputForm v-bind:messages="messages"></inputForm>' +
        ' <div class="card-header mb-2">Messages:</div>' +
        '  <messagesChat v-bind:profile="profile" :messages="messages"></messagesChat>' +
        '</div>'
});

Vue.component('roomsList', {
    props: ['usersRooms'],
    template: ' <div><li v-for="userRooms in usersRooms" class="list-group-item list-group-item-action"\n' +
        '                            v-on:click="selectUser()">' +
        '                            {{userRooms.name}}' +
        '                        </li></div>',
    methods: {
        selectUser: function (name) {

            var room = {
                name: 'private',
                participantsName: [name]
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    //todo: extract to function
                    while(frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function(item) {
                        frontendData.messages.push(item);
                    });
                })
            )

        }
    }
});


Vue.component('user-row', {
    props: ['user', 'rooms'],
    data: function () {
        return {
            name: 'private',
            participantsName: ['name']
        }
    },
    template:
        '<div class="list row">' +
        ' <div class="col-md-6">' +
        ' <ul> <li class="list-group-item list-group-item-action">{{user.name}}  </li> </ul> </div> </div>',


});

Vue.component('users-list', {
    props: ['users'],
    template:
        '<div>' +

        '<user-row v-for="user in users"  v-on="open" :key="user.id" :user="user"/>' +
        '</div>',
    methods: {
        open: function (event) {
            var room = {
                name: this.name,
                participantsName: this.participantsName
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    this.rooms.push(data);
                })
            )

        }
    }
});
Vue.component('messagesChat', {
    props:['messages', 'profile'],
    template: ' <div class="card-body">\n' +
        ' <ul class="list-group">\n' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</ul> </div>'
});

Vue.component('leftHeadPanel', {
    props:['profile'],
    template: '<div class="card-header">' +
'                <div class="row">' +
'                    <div class="col-sm">\n' +
'                        <div v-if="!profile">Guest</div>\n' +
'                        <div v-else>{{profile.name}}</div>\n' +
'                    </div>' +
'                    <div class="col-sm">\n' +
'                        <div v-if="profile">\n' +
'                            <div class="col-sm">\n' +
'                                <a class=" btn-primary btn-block mb-2" href="/logout">Log out</a>\n' +

'                            </div>\n' +
'                            <div class="col-sm">\n' +
'                                <button type="button" @click="showModal = true" class="btn-primary btn-block mb-2" data-toggle="modal"\n' +
'                                           >\n' +
'                                    Create room\n' +
'                                </button>\n' +

'                            </div>\n' +
'                        </div>\n' +
'                        <div v-else>\n' +
'                            <div class="col-sm">\n' +
'                                <a class=" btn-primary btn-block mb-2" href="/login">Login</a>\n' +

'                            </div>\n' +
'                        </div>\n' +
'                    </div>\n' +

'                </div>\n' +
'            </div>'

});
Vue.component('leftPanelBody', {
    props: ['profile', 'usersRooms', 'users'],
    template: '<div><div v-if="profile" class="card-body">\n' +
        '                <div class="list-group">\n' +
        '                    <div class="card-header">Users</div>\n' +
        '                    <ul class="list-group">' +
        '                        <usersList :users="users"></usersList>\n' +
        '                    </ul>' +
        '                    <div class="card-header">Chats</div>\n' +
        '                    <ul class="list-group">\n' +
        '                        <roomsList :usersRooms="usersRooms"></roomsList>\n' +
        '                    </ul>' +
        '                </div>' +
        '            </div></div>'
})

Vue.component('leftPanel', {
    props: ['profile', 'usersRooms', 'users'],
    template:  '<div>' +
        '<leftHeadPanel v-bind:profile="profile"></leftHeadPanel>' +
        '<leftPanelBody v-bind:profile="profile" v-bind:users="users" v-bind:usersRooms="usersRooms"></leftPanelBody>' +
        '</div>'

    });

var app = new Vue({
    el: '#app',
    template: '<div>' +
        //tittle
        '<modal v-if="showModal" @close="showModal = false">' +
        '<h3 slot="header">custom header</h3>\n' +
        '  </modal>' +
        '<div class="row mb-3">' +
        '        <div class="col">' +
        '            <h1>Chat application </h1>' +
        '            <hr>' +
        '        </div></div>' +
        '    <div class="card-deck mb-3 text-center">' +
        '        <div class="card mb-4 shadow-sm">' +
        '<leftPanel v-bind:users="users" v-bind:profile="profile" v-bind:usersRooms="usersRooms"></leftPanel>' +
        '        </div>' +
        '        <div class="card mb-2 shadow-sm">' +
        '  <rightPanel v-bind:messages="messages" v-bind:profile="profile"></rightPanel>' +
        '    </div></div>' +
        '    <hr>' +
        '</div>',
    /* template: '<div class="col-md-4">' +

         '<div v-if="!profile"><h4>You can authorize</h4> <a href="/login">here</a> </div>' +
         '<div v-if="!profile"><h4>or add new profile</h4> <a href="/registration">here</a> </div>' +
         '<div v-else> <div>{{profile.name}}&nbsp;<a href="/logout">Log out</a>' +
         '<div><users-list :users="users" /> </div>' +

         '</div></div>' +
         '<messages-list :messages="messages"/>' +

         '</div>',*/
    data: function () {
        return {
            users: frontendData.users,
            messages: frontendData.messages,
            profile: frontendData.profile,
            rooms: frontendData.rooms,
            usersRooms: frontendData.usersRooms,
            messagesInRoom: frontendData.messagesInRoom,
            showModal: false,
            currentRoomId: frontendData.currentRoomId
        }
    }
});