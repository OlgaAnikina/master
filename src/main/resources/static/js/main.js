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
            };
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    console.log(data);
                    if (!frontendData.rooms.includes(data)) {
                        frontendData.rooms.push(data);
                    }

                    frontendData.currentRoomId = data.id;
                    frontendData.currentRoom.id = data.id;
                    frontendData.currentRoom.name = data.name;
                    frontendData.currentRoom.participantsName = data.participantsName;
                    frontendData.currentRoom.type = data.type;


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
        '<div v-if="profile == null && message.authorName==\'Guest\' || profile && profile.name == message.authorName" class="selfMessage">' +
        '<li class="list-group-item list-group-item-action mb-2 block1">' +
        '<div class="list-;group  ">' +
        '<div>{{message.text}}</div>' +
        '<div class="author"><i>{{message.authorName}}</i></div>' +
        '<div>{{message.createdWhen}}</div>' +

        '</div></li></div>' +

        '<div v-else>' +
        '<li class="list-group-item list-group-item-action mb-2 block1">' +
        '<div class="list-group ">' +
        '<div>{{message.text}}</div>' +
        '<div class="author"><i>{{message.authorName}}</i></div>' +
        '<div class="author">{{message.createdWhen}}</div>' +
        '</div></li></div>' +
        '</div>',
    methods: {
        time() {
            var now = new Date();

            return now;
        }
    }
});

Vue.component('inputForm', {
    props: ['messages'],
    data: function () {
        return {
            text: '',
        }
    },
    computed: {
        isComplete() {
            return this.text;
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
        '<input type="button"  :disabled=\'!isComplete\' class="btn-lg btn-primary mb-2 button" value="Save" @click="save(text)"/>' +
        '</div></div>',
    methods: {

        save: function (text) {
            var message = {text: text, roomId: frontendData.currentRoomId, createdWhen: new Date().getTime()};
            messageApi.save({}, message).then(result =>
                result.json().then(data => {
                  //  frontendData.messages.push(data);
                })
            )
        }
    }
});

Vue.component('modal', {
    props: ['users', 'profile'],
    data: function () {
                return {
                    checkedUsers: [],
                    roomName: '',
                    answer: 'Input form is empty'

                }
            },
    computed: {
        isComplete() {
            return this.roomName && this.checkedUsers;
        },
        isNotEmptyName() {
            return this.roomName;
        }
    },
    template: '<div>' +
        '<transition name="modal-fade">' +
        '<div class="modal-container" role="dialog">' +
        '<div class="modal-block">' +
        '<div  ref="modal" class="modal-content">' +
        '<header class="modal-header">' +
        '<slot name="header">' +
        '<h2>Create new chat</h2>' +
        '<button type="button" class="btn-primary btn-block-modal mb-2"' +
        ' @click="initClose" aria-label="Close modal">x</button>' +
        '</slot>' +
        '</header>' +

        '<section class="modal-body">' +
        '<slot name="body">' +
        '<br>' +
        '<form class="was-validated">' +
        '<div class="mb-3">' +
        '<label for="validationTextarea">Input chat\'s name:</label>' +
        '<input type="text" placeholder="Name of new chat" v-model="roomName"/>' +
        ' <p>{{ answer }}</p>' +
        '</div>' +
        '<br>' +
        '<div class="card-header">Choose participants to chat:</div>' +

        '<ul class="list-group list-group-flush list-users">' +
        '<li v-for="user in users" class="list-group-item"  >' +
        '{{user.name}}' +
        '<label class="checkbox" >' +
        '<input type="checkbox"  v-bind:value="user.name" v-model="checkedUsers" />' +
        '<span class="primary"></span>' +
        '</label>\n' +
        '</li></ul>' +
        '<br>' +
        '</form>' +
        '</slot>' +
        '</section>' +
        '<footer class="modal-footer">' +
        '<slot name="footer">' +
        '<button type="button" :disabled=\'!isComplete\'  class="btn-primary btn-block mb-2" ' +
        '@click="createRoom(roomName, checkedUsers )" aria-label="Close modal">Create' +
        '</button>' +
        '</slot>' +
        '</footer>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</transition>' +
        '</div>',
  /*  watch: {

    roomName: function (newRoomName, oldRoomName) {
        this.answer = 'Input ...'
        this.debouncedGetAnswer
    }
    },
    created: function () {
        this.debouncedGetAnswer = this.getAnswer();
       //this.getAnswer();
    },*/
    methods: {
    getAnswer: function () {
        var vm = this;
        this.answer = 'Wait...';
        var checkRoomName = setInterval(() => {
            if (this.isNotEmptyName) {
                this.$http.get('/v2/room/checkName/' + this.roomName).then(response => {

                    if (response.body != null) {

                        vm.answer = response.data.answer;
                        vm.answer = response.body;
                    }

                }, response => {
                    vm.answer = 'Can\'t check room\'s name. '
                });

            }
        }, 1000)

       },


        initClose() {
            this.$emit('close')
        },
        createRoom: function (roomName, checkedUsers) {

            var room = {
                name: roomName,
                participantsName: checkedUsers,
                type: "PUBLIC"
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    console.log(data);
                    if (!frontendData.usersRooms.includes(data)) {
                        frontendData.usersRooms.push(data);
                    }

                    frontendData.currentRoomId = data.id;
                    frontendData.currentRoom.id = data.id;
                    frontendData.currentRoom.name = data.name;
                    frontendData.currentRoom.participantsName = data.participantsName;
                    frontendData.currentRoom.type = data.type;

                    while (frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function (item) {
                        frontendData.messages.push(item);
                    });
                })
            );
            this.$emit('close')

        }
    }
});

Vue.component('addParticipants', {
    props: ['currentRoom', 'users'],
    data: function () {
        return {
            checkedUsers: [],
            }
    },
    computed: {
        isComplete() {
            return this.checkedUsers;
        }
    },
    template: '<div>' +
        '<transition name="modal-fade">' +
        '<div class="modal-container" role="dialog">' +
        '<div class="modal-block">' +
        '<div  ref="modal" class="modal-content">' +
        '<header class="modal-header">' +
        '<slot name="header">' +
        '<h2>Chats\' property:</h2>' +
        '<button type="button" class="btn-primary btn-block-modal mb-2"' +
        ' @click="initClose" aria-label="Close modal">x</button>' +
        '</slot>' +
        '</header>' +

        '<section class="modal-body">' +
        '<slot name="body">' +
        '<br>' +
        '<form class="was-validated">' +
        '<div class="mb-3">' +
        '<div class="card-header"> Users in this chat:</div>' +
        '<ul class="list-group list-group-flush list-users">' +
        '<div v-for="name in currentRoom.participantsName">' +
        '<li>{{name}}</li></div></ul>' +
        '</div>' +
        '<div class="card-header">Choose participants to chat:</div>' +
        '<ul class="list-group list-group-flush list-users">' +
        '<li v-for="user in users" class="list-group-item" v-if="!currentRoom.participantsName.includes(user.name)"  >' +
        '<div v-if="!currentRoom.participantsName.includes(user.name) ">{{user.name}}' +
        '<label class="checkbox" >' +
         '<input type="checkbox"  v-bind:value="user.name" v-model="checkedUsers" />' +
         '<span class="primary"></span>' +
         '</label></div>' +
        '</li></ul>' +
    //    '<span>Отмеченные имена: {{ checkedUsers }}</span>' +
        '</form>' +
        '</slot>' +
        '</section>' +
        '<footer class="modal-footer">' +
        '<slot name="footer">' +
        '<button type="button" :disabled=\'!isComplete\'  class="btn-primary btn-block mb-2" ' +
        '@click="addUsers(checkedUsers, currentRoom)" aria-label="Close modal">Add participants' +
        '</button>' +
        '</slot>' +
        '</footer>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</transition>' +
        '</div>',
    methods: {

        initClose() {
            this.$emit('close')
        },
        addUsers: function (checkedUsers, currentRoom) {
            console.log(checkedUsers);
            frontendData.currentRoom.participantsName.push(checkedUsers);

            var room = {
                name: currentRoom.name,
                participantsName:  currentRoom.participantsName,
                type: currentRoom.type
            }

            roomApi.update({roomId: frontendData.currentRoomId}, checkedUsers)
                .then(response => {
                    response.json().then(data => {

                    }, response => {

                    })
                })

            this.$emit('close')

        }

    }
});

Vue.component('infOfRoom', {
    data: function () {
        return {
            isModalAdd: false
        }
    },
    computed: {
        isPublic() {
            return frontendData.currentRoom.type == 'PUBLIC' || frontendData.currentRoomId == 0;
        }
    },
    props: ['currentRoom', 'users', 'profile'],
    template: '<div>' +
        '<div v-if="isPublic">' +
        '<div class="card-header">' +
        '<div class="row">' +
        '<div class="col-sm">' +
        '<div class="otherMessage">Chat\'s name: ' +
        '{{currentRoom.name}} </div>' +
        '<div v-if="profile != null" class="col-sm selfMessage">' +
        '<button type="button" @click="isModalAdd = true" class="btn-primary btn-style mb-2">' +
        'Chat\'s property</button>' +
        '</div></div>' +
        '</div></div>' +
        '<addParticipants  v-show="isModalAdd" v-bind:users="users"  :currentRoom="currentRoom"' +
        ' @close="closeModal" />' +
        '</div>' +
        '</div>',
    methods: {
        showModal() {
            this.isModalAdd = true;
        },
        closeModal() {
            this.isModalAdd = false;
        }
    }
});

Vue.component('rightPanel', {
    props: ['messages', 'profile', 'currentRoom', 'users'],
    template: '<div>' +
        '<inputForm v-bind:messages="messages"></inputForm>' +
        '<div v-if="currentRoom.type== \'PUBLIC\'"' +
        '<infOfRoom v-bind:users="users" :currentRoom="currentRoom" v-bind:profile="profile"></infOfRoom>' +
        '<div class="card-header mb-2">Messages:</div>' +
        '<messagesChat v-bind:profile="profile" :messages="messages"></messagesChat>' +
        '</div>'
});

Vue.component('roomsList', {
    props: ['usersRooms'],
    template: '<div><li v-for="userRooms in usersRooms"' +
        ' class="list-group-item list-group-item-action" v-on:click="selectRoom(userRooms)">' +
        '{{userRooms.name}}' +
        '</li></div>',
    methods: {
        selectRoom: function (userRooms) {

            var room = {
                id: userRooms.id,
                name: userRooms.name,
                participantsName: userRooms.participantsName,
                type: userRooms.type
            }
            roomApi.save({}, room).then(result =>
                result.json().then(data => {
                    console.log(data);
                    if (!frontendData.rooms.includes(data)) {
                        frontendData.rooms.push(data);
                    }

                    frontendData.currentRoomId = data.id;
                    frontendData.currentRoom.id = data.id;
                    frontendData.currentRoom.name = data.name;
                    frontendData.currentRoom.participantsName = data.participantsName;
                    frontendData.currentRoom.type = data.type;

                    while (frontendData.messages.length > 0) {
                        frontendData.messages.pop();
                    }
                    data.messages.forEach(function (item) {
                        frontendData.messages.unshift(item);
                    });
                })
            )

        }
    }
});

Vue.component('messagesChat', {
    props: ['messages', 'profile'],
    template: '<div  class="card-body" id="scroll">' +
        '<ul class="list-group list-messages">' +
        '<message-row  v-bind:profile="profile" v-for="message in messages" :key="message.id" :message="message"/>' +
        '</ul></div>',
    methods: {
        scrollToEnd() {
            var container = document.getElementById("scroll");
            var scrollHeight = container.scrollHeight;
            if (scrollHeight > 340 && !container.classList.contains('block-messages')) {
                var d = document.getElementById("scroll");
                d.className += " block-messages";
            } else if (container.classList.contains('block-messages') && scrollHeight < 350) {
                container.classList.remove('block-messages');
            }
        }
    },
    mounted() {
        this.scrollToEnd();
    },
    updated() {
        this.scrollToEnd();

    }
});

Vue.component('leftHeadPanel', {
    data: function () {
        return {
            isModalVisible: false
        }
    },
    props: ['profile', 'users'],
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
        '<button type="button" @click="isModalVisible = true" class="btn-primary btn-block mb-2">' +
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

        '<modal  v-show="isModalVisible" v-bind:users="users"  v-bind:profile="profile"' +
        ' @close="closeModal" />' +
        '</div>' +
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

Vue.component('leftPanelBody', {
    props: ['profile', 'usersRooms', 'users'],
    template: '<div><div v-if="profile" class="card-body">' +
        '<div class="list-group">' +
        '<div class="card-header">Users</div>' +
        '<ul class="list-group list-users">' +
        '<usersList v-bind:profile="profile" :users="users"></usersList>' +
        '</ul>' +
        '<div class="card-header">Chats</div>' +
        '<ul class="list-group list-chats">' +
        '<roomsList :usersRooms="usersRooms"></roomsList>' +
        '</ul>' +
        '</div>' +
        '</div></div>'

});

Vue.component('leftPanel', {
    props: ['profile', 'usersRooms', 'users', 'isModalVisible', 'currentRoom'],
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
        '<div class="card  shadow-sm">' +
        '<leftPanel v-bind:users="users" v-bind:profile="profile"' +
        ' v-bind:usersRooms="usersRooms" v-bind:currentRoom="currentRoom"></leftPanel>' +
        '</div>' +
        '<div class="card shadow-sm">' +
        '<rightPanel  v-bind:users="users" v-bind:messages="messages" v-bind:profile="profile"' +
        ' v-bind:currentRoom="currentRoom"></rightPanel>' +
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
            currentRoom: frontendData.currentRoom

        }
    },
    computed: {
        isLastId() {
            if(frontendData.messages.length != 0) {
                var lastId = frontendData.messages[0].id ;
                console.log(lastId);
                for (var i = 0; i < frontendData.messages.length; i++ ) {

                    if(lastId < frontendData.messages[i].id) {
                        lastId = frontendData.messages[i].id;
                    }
                }}
            return lastId;
        }
    },
    methods: {
        isLastId() {
            if(frontendData.messages.length != 0) {
                var lastId = frontendData.messages[0].id ;
                console.log(lastId);
                for (var i = 0; i < frontendData.messages.length; i++ ) {

                    if(lastId < frontendData.messages[i].id) {
                        lastId = frontendData.messages[i].id;
                    }
                }} else lastId = 0;
            return lastId;
        },
        idLastRoom() {
            if (frontendData.usersRooms.length != 0) {
                var lastRoom = frontendData.usersRooms[0].id;
                for(var i = 0; i < frontendData.usersRooms.length; i++) {
                    if(lastRoom < frontendData.usersRooms[i].id) {
                        lastRoom = frontendData.usersRooms[i].id;
                    }
                }
            }else lastRoom = 0;
            return lastRoom;
        },
        idLastUser() {
            if (frontendData.users.length != 0) {
                var lastUser = frontendData.users[0].id;
                for(var i = 0; i < frontendData.users.length; i++) {
                    if(lastUser < frontendData.users[i].id) {
                        lastUser = frontendData.users[i].id;
                    }
                }
            }else lastUser = 0;
            return lastUser;
        },
         pollData() {

             if (frontendData.profile != null) {
             var updateMessages = setInterval(() => {
               this.$http.get('/v2/rooms/' + frontendData.currentRoomId + '/messages/' + this.isLastId() /*'?lastId=' + lastId*/).then(response => {

                     response.body.forEach(function (item) {
                         frontendData.messages.push(item);
                     });

                 }, response => {});


             }, 1000);
             var updateRooms = setInterval(() => {
                 this.$http.get('/v2/room/' + this.idLastRoom()).then(response => {

                     if (response.body != null){

                         response.body.forEach(function (item) {
                             frontendData.usersRooms.push(item);
                         });
                     }

                 }, response => {});


             }, 1000);
             var updateUsers = setInterval(() => {
                 this.$http.get('v2/users/' + this.idLastUser()).then(response => {

                     if (response.body != null){

                         response.body.forEach(function (item) {
                             frontendData.users.push(item);
                         });
                     }

                 }, response => {});


             }, 1000)

         }
        }
     },
     created() {
         this.pollData();
     }
});


