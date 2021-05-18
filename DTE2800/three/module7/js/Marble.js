// Based on heavily modified code from example-code "TiltableTerrain" made by Werner Farstad

"use strict";
import * as THREE from "../../lib/three/build/three.module.js";
import {ColGroups} from "./Constants.js";

export class Marble{
    constructor(scene, physicsWorld) {
        this.scene = scene;
        this.physicsWorld = physicsWorld;
        this.marbleMesh = {};
    }

    createRandomMarble(){
        let xPos = -14 + Math.random() * 28;
        let zPos = 15 + Math.random() * -50;
        let pos = {x: xPos, y: 50, z: zPos};
        this.createMarble(pos);
    }

    createMarble(pos={x:0,y:50,z:0}, mass=1, color='#FF0505'){
        let radius = 1;
        let quat = {x: 0, y: 0, z: 0, w: 1};

        //AMMO:
        let transform = new Ammo.btTransform();
        transform.setOrigin( new Ammo.btVector3( pos.x, pos.y, pos.z ) );
        transform.setRotation( new Ammo.btQuaternion( quat.x, quat.y, quat.z, quat.w ) );
        let motionState = new Ammo.btDefaultMotionState( transform );
        let marbleShape = new Ammo.btSphereShape( radius );
        marbleShape.setMargin( 0.05 );
        let localInertia = new Ammo.btVector3( 0, 0, 0 );
        marbleShape.calculateLocalInertia( mass, localInertia );
        let rbInfo = new Ammo.btRigidBodyConstructionInfo( mass, motionState, marbleShape, localInertia );
        let sphereRigidBody = new Ammo.btRigidBody( rbInfo );
        sphereRigidBody.setRestitution(0.01);
        sphereRigidBody.setFriction(0.2);
        this.physicsWorld.addRB( sphereRigidBody, ColGroups.Sphere, ColGroups.Sphere | ColGroups.Plane);

        //THREE
        this.marbleMesh = new THREE.Mesh(new THREE.SphereBufferGeometry(radius, 32, 32), new THREE.MeshPhongMaterial({color: color}));
        let origin = transform.getOrigin();
        let orientation = transform.getRotation();
        this.marbleMesh.position.set(origin.x(), origin.y(), origin.z());
        this.marbleMesh.setRotationFromQuaternion(new THREE.Quaternion(orientation.x(), orientation.y(), orientation.z(), orientation.w()));
        this.marbleMesh.castShadow = true;
        this.marbleMesh.receiveShadow = true;
        this.scene.add(this.marbleMesh);
        this.marbleMesh.userData.physicsBody = sphereRigidBody;
        this.marbleMesh.userData.name = 'marbleMesh';
        this.physicsWorld.rigidBodies.push(this.marbleMesh);
    }
}