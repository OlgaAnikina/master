var messageApi = Vue.resource('/v1/messages{/id}{?roomId}');
var roomApi = Vue.resource('/v2/room{/roomId}');

Vue.component('user', {
    props: ['user', 'profile'],
    template: '<div>' +
        '<li class="list-group-item list-group-item-action"\n' +
        '                            v-on:click="selectUser(user.name,profile.name)">' +
        ' {{user.name}}' +
        '</li>' +
        '</div>',
    methods: {
        selectUser: function (name, profile) {

            var room = {
                name: profile + '/' + name,
                participantsName: [name],
                type: "PRIVATE"
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    console.log(data);
                    if (!frontendData.rooms.includes(data)) {
                        frontendData.rooms.push(data);
                    }

                    frontendData.currentRoomId = data.id;

                    //todo: extract to function
                    while (frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function (item) {
                        frontendData.messages.push(item);
                    });
                })
            )

        }
    }

});

Vue.component('usersList', {
    props: ['users', 'profile'],
    template: '<div class="card-body">' +
        '<ul class="list-group">' +
        '<user v-bind:profile="profile" v-for="user in users" :key="user.id" :user="user"/>' +
        '</ul></div>'
});

Vue.component('message-row', {
    props: ['message', 'profile'],
    template: '<div> ' +

        '<li class="list-group-item list-group-item-action mb-2 block1">' +
        '<div class="list-group">' +
        '  <div>{{message.text}}</div>' +
        '  <div class="author"><i>{{message.authorName}}</i>' +
        '</div></div>' +
        ' </li></div>' +
        '</div>'
});

Vue.component('inputForm', {
    props: ['messages'],
    data: function () {
        return {
            text: '',
        }
    },
    template: '<div class="card-header">' +
        '<div class="form-group">' +
        '<form class="was-validated">' +
        '<div class="mb-3">' +
        '<label for="validationTextarea">Input message:</label>' +
        '<textarea v-model="text" class="form-control is-invalid"' +
        'id="validationTextarea" placeholder="Required example textarea" required></textarea>' +

        '</div></form>' +
        '<input type="button" class="btn-lg btn-primary mb-2 button" value="Save" @click="save(text)"/>' +
        '</div></div>',
    methods: {

        save: function (text) {
            var message = {text: text, roomId: frontendData.currentRoomId};
            messageApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.messages.push(data);
                })
            )
        }
    }
});

Vue.component('modal', {
    props: ['isModalVisible', 'users'],
    data: function () {
        return {
            checkedUsers: [],
            roomName: ''
        }
    },
    template: '<div>' +
        '<transition name="modal-fade">' +
        '<div class="modal-container" role="dialog">' +
        '<div  ref="modal">' +
        '<header class="modal-header">' +
        '<slot name="header">' +
        '<h2>Create new chat</h2>' +
        '<button type="button" class="btn-primary btn-block-modal mb-2"' +
        ' @click="close" aria-label="Close modal">x</button>' +
        '</slot>' +
        '</header>' +

        '<section class="modal-body">' +
        '<slot name="body">' +
        '<br>' +
        '<form class="was-validated">' +
        '<div class="mb-3">' +
        '<label for="validationTextarea">Input chat\'s name:</label>' +
        '<text v-model="text" ' +
        'id="validationTextarea" ></text>' +

        '</div>' +
        '<br>' +
        '<div>' +
             '<div type="checkbox" v-for="user in users"' +
                ' class="list-group-item list-group-item-action" >' +
              //  '<label>{{user.name}}</label>' +
                '<input type = checkbox  id="{{user.name}}"   v-model="checkedUsers" >' +
                '<label>{{user.name}}</label>' +
        '</div>' +
        '</div>' +
        '  <br>\n' +
        '  <span>Отмеченные имена: {{ checkedUsers }}</span>\n' +
       '</form>' +
        '</slot>' +
        '</section>' +


        '<footer class="modal-footer">' +
        '<slot name="footer">' +
        '<button type="button" class="btn-primary btn-block mb-2" ' +
        '@click="close" aria-label="Close modal">Create' +
        '</button>' +
        '</slot>' +
        '</footer>' +
        '</div>' +
        '</div>' +
        '</transition>' +
        '</div>',
    methods: {
        showModal() {
            this.isModalVisible = true;
        },
        closeModal() {
            this.isModalVisible = false;
        }
    }
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
    template: '<div><li v-for="userRooms in usersRooms"' +
        ' class="list-group-item list-group-item-action" v-on:click="selectRoom()">' +
        '{{userRooms.name}}' +
        '</li></div>',
    methods: {
        selectRoom: function (name) {

            var room = {
                name: name,
                participantsName: [name]
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    //todo: extract to function
                    while (frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function (item) {
                        frontendData.messages.push(item);
                    });
                })
            )

        }
    }
});

Vue.component('messagesChat', {
    props: ['messages', 'profile'],
    template: ' <div class="card-body">\n' +
        ' <ul class="list-group">\n' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</ul> </div>'
});

Vue.component('leftHeadPanel', {
    props: ['profile', 'isModalVisible', 'users'],
    template: '<div><div class="card-header">' +
        '<div class="row">' +
        '<div class="col-sm">' +
        '<div v-if="!profile">Guest</div>' +
        '<div v-else>{{profile.name}}</div>' +
        '</div>' +
        '<div class="col-sm">' +
        '<div v-if="profile">' +
        '<div class="col-sm">' +
        '<a class="btn-primary btn-block mb-2" href="/logout">Log out</a>' +

        '</div>' +
        '<div class="col-sm">' +
        '<button type="button" @click="showModal" class="btn-primary btn-block mb-2">' +
        'Create room</button>' +

        '</div>' +
        '</div>' +
        '<div v-else>' +
        '<div class="col-sm">' +
        '<a class="btn-primary btn-block mb-2" href="/login">Login</a>' +

        '</div>' +
        '</div>' +
        '</div>' +

        '</div></div>' +

        /* '<modal v-bind:showModal="showModal" v-if="showModal" @close="showModal = false">' +
         ' <h3 slot="header">custom header</h3>'+
         '</modal>'+*/
        '<modal  v-show="isModalVisible" v-bind:users="users" ' +
        'v-bind:isModalVisible="isModalVisible" @close="closeModal" />' +
        '</div>' +
        '  </div>',
    methods: {
        showModal() {
            this.isModalVisible = true;
        },
        closeModal() {
            this.isModalVisible = false;
        }
    }


});

Vue.component('leftPanelBody', {
    props: ['profile', 'usersRooms', 'users'],
    template: '<div><div v-if="profile" class="card-body">\n' +
        '                <div class="list-group">\n' +
        '                    <div class="card-header">Users</div>\n' +
        '                    <ul class="list-group">' +
        '                        <usersList v-bind:profile="profile" :users="users"></usersList>\n' +
        '                    </ul>' +
        '                    <div class="card-header">Chats</div>\n' +
        '                    <ul class="list-group">\n' +
        '                        <roomsList :usersRooms="usersRooms"></roomsList>\n' +
        '                    </ul>' +
        '                </div>' +
        '            </div></div>'

});

Vue.component('leftPanel', {
    props: ['profile', 'usersRooms', 'users', 'isModalVisible'],
    template: '<div>' +
        '<leftHeadPanel v-bind:profile="profile" v-bind:isModalVisible="isModalVisible"' +
        ' v-bind:users="users" ></leftHeadPanel>' +
        '<leftPanelBody v-bind:profile="profile" v-bind:users="users" v-bind:usersRooms="usersRooms"></leftPanelBody>' +
        '</div>'

});

var app = new Vue({
    el: '#app',
    template: '<div>' +

        '<div class="row mb-3">' +
        '<div class="col">' +
        '<h1>Chat application </h1>' +
        '<hr>' +
        '</div></div>' +
        '<div class="card-deck mb-3 text-center">' +
        '<div class="card mb-4 shadow-sm">' +
        '<leftPanel v-bind:users="users" v-bind:profile="profile"' +
        ' v-bind:usersRooms="usersRooms" v-bind:isModalVisible="isModalVisible"></leftPanel>' +
        '</div>' +
        '<div class="card mb-2 shadow-sm">' +
        '<rightPanel v-bind:messages="messages" v-bind:profile="profile"></rightPanel>' +
        '</div></div>' +
        '<hr>' +
        '</div>',

    data: function () {
        return {
            users: frontendData.users,
            messages: frontendData.messages,
            profile: frontendData.profile,
            rooms: frontendData.rooms,
            usersRooms: frontendData.usersRooms,
            messagesInRoom: frontendData.messagesInRoom,
            currentRoomId: frontendData.currentRoomId,
            isModalVisible: false,
        }
    }
});