// Based on heavily modified code from example-code "TiltableTerrain" made by Werner Farstad
// All textures are imported from OpenGameArt.com

"use strict";
import * as THREE from "../../lib/three/build/three.module.js";
import {ColGroups, Axis} from "./Constants.js";

export class GameBoard{
    constructor(scene, physicsWorld) {
        this.scene = scene;
        this.physicsWorld = physicsWorld;
        this.groupMesh = {};
        this.size={width:100, height:2, depth:100}
    }

    createGameBoard(){

        //Ammo-konteiner:
        this.compoundShape = new Ammo.btCompoundShape();

        //Three-konteiner:
        this.groupMesh = new THREE.Group();
        this.groupMesh.position.x = 0
        this.groupMesh.position.y = 0;
        this.groupMesh.position.z = 0;

        /**/
        // textures from OpenGameArt.com
        let tBox = new THREE.TextureLoader().load('./res/wood_texture_512_512.jpg')
        let boxMaterial = new THREE.MeshPhongMaterial( {side: THREE.DoubleSide, map: tBox} );
        this.addGameBoardPlane();
        //wallFront
        this.addBox({width:this.size.width, height:this.size.height*2, depth:2},{x:0, y:this.size.height*2, z:1-this.size.depth/2}, boxMaterial);
        //wallBack
        this.addBox({width:this.size.width, height:this.size.height*2, depth:2},{x:0, y:this.size.height*2, z:this.size.depth/2-1}, boxMaterial);
        //wallLeft
        this.addBox({width:this.size.width, height:this.size.height*2, depth:2},{x:1-this.size.depth/2, y:this.size.height*2, z:0}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        //wallRight
        this.addBox({width:this.size.width, height:this.size.height*2, depth:2},{x:-1+this.size.depth/2, y:this.size.height*2, z:0}, boxMaterial,{x:0, y:Math.PI/2, z:0})

        //obstacles
        //horizontal
        this.addBox({width:16, height:this.size.height*2, depth:2},{x:-40, y:this.size.height*2, z:-40}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:28, height:this.size.height*2, depth:2},{x:-10, y:this.size.height*2, z:-40}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:40, height:this.size.height*2, depth:2},{x:0, y:this.size.height*2, z:0}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:18, height:this.size.height*2, depth:2},{x:40, y:this.size.height*2, z:0}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:16, height:this.size.height*2, depth:2},{x:-40, y:this.size.height*2, z:38}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:16, height:this.size.height*2, depth:2},{x:-40, y:this.size.height*2, z:8}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:28, height:this.size.height*2, depth:2},{x:-18, y:this.size.height*2, z:23}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:28, height:this.size.height*2, depth:2},{x:22, y:this.size.height*2, z:12}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:32, height:this.size.height*2, depth:2},{x:32, y:this.size.height*2, z:23}, boxMaterial,{x:0, y:0, z:0});
        this.addBox({width:28, height:this.size.height*2, depth:2},{x:22, y:this.size.height*2, z:34}, boxMaterial,{x:0, y:0, z:0});

        //Vertical
        this.addBox({width:12, height:this.size.height*2, depth:2},{x:30, y:this.size.height*2, z:-42}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:18, height:this.size.height*2, depth:2},{x:30, y:this.size.height*2, z:-20}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:12, height:this.size.height*2, depth:2},{x:10, y:this.size.height*2, z:-17}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:19, height:this.size.height*2, depth:2},{x:10, y:this.size.height*2, z:-39}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:30, height:this.size.height*2, depth:2},{x:-10, y:this.size.height*2, z:-16}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:30, height:this.size.height*2, depth:2},{x:-34, y:this.size.height*2, z:-14}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:14, height:this.size.height*2, depth:2},{x:-20, y:this.size.height*2, z:31}, boxMaterial,{x:0, y:Math.PI/2, z:0});
        this.addBox({width:8, height:this.size.height*2, depth:2},{x:20, y:this.size.height*2, z:44}, boxMaterial,{x:0, y:Math.PI/2, z:0});



        // Ammo compoundShape, transformasjon & rigidBody:
        let massTerrain = 0;
        let compoundShapeTrans = new Ammo.btTransform();
        compoundShapeTrans.setIdentity();
        compoundShapeTrans.setOrigin(new Ammo.btVector3(this.groupMesh.position.x,this.groupMesh.position.y,this.groupMesh.position.z));
        let quatCompound = this.groupMesh.quaternion;
        compoundShapeTrans.setRotation( new Ammo.btQuaternion( quatCompound.x, quatCompound.y, quatCompound.z, quatCompound.w ) );
        let motionState = new Ammo.btDefaultMotionState( compoundShapeTrans );
        let localInertia = new Ammo.btVector3( 0, 0, 0 );
        this.compoundShape.setLocalScaling(new Ammo.btVector3(this.groupMesh.scale.x,this.groupMesh.scale.y, this.groupMesh.scale.x));
        this.compoundShape.calculateLocalInertia( massTerrain, localInertia );
        let rbInfo = new Ammo.btRigidBodyConstructionInfo( massTerrain, motionState, this.compoundShape, localInertia );
        let terrainRigidBody = new Ammo.btRigidBody(rbInfo);
        terrainRigidBody.setFriction(0.3);
        terrainRigidBody.setRestitution(0.03);
        terrainRigidBody.setCollisionFlags(terrainRigidBody.getCollisionFlags() | 2);   // BODYFLAG_KINEMATIC_OBJECT = 2 betyr kinematic object, masse=0 men kan flyttes!!
        terrainRigidBody.setActivationState(4);   // Never sleep, BODYSTATE_DISABLE_DEACTIVATION = 4
        this.scene.add(this.groupMesh);

        // Legg til physicsWorld:
        this.physicsWorld.addRB( terrainRigidBody, ColGroups.Plane, ColGroups.Sphere | ColGroups.Plane);
        this.physicsWorld.updateSingleAabb(terrainRigidBody) ;
        this.groupMesh.userData.physicsBody = terrainRigidBody;
        this.physicsWorld.rigidBodies.push(this.groupMesh);
    }
    

// axisNo: 1=x, 2=y, 3=z
    tilt(axisNo, angle) {
        let rotAxis;
        switch (axisNo) {
            case Axis.x:
                rotAxis = new THREE.Vector3( 1, 0, 0 );
                break;
            case Axis.y:
                rotAxis = new THREE.Vector3( 0,1, 0);
                break;
            case Axis.z:
                rotAxis = new THREE.Vector3( 0,0, 1);
                break;
            default:
                rotAxis = new THREE.Vector3( 1, 0, 0 );
        }
        // Henter gjeldende transformasjon:
        let terrainTransform = new Ammo.btTransform();
        let terrainMotionState = this.groupMesh.userData.physicsBody.getMotionState();
        terrainMotionState.getWorldTransform( terrainTransform );
        let ammoRotation = terrainTransform.getRotation();

        // Roter gameBoardRigidBody om en av aksene (bruker Three.Quaternion til dette):
        let threeCurrentQuat = new THREE.Quaternion(ammoRotation.x(), ammoRotation.y(), ammoRotation.z(), ammoRotation.w());
        let threeNewQuat = new THREE.Quaternion();
        threeNewQuat.setFromAxisAngle(rotAxis, this.toRadians(angle));
        // Slår sammen eksisterende rotasjon med ny/tillegg.
        let resultQuaternion = threeCurrentQuat.multiply(threeNewQuat);
        // Setter ny rotasjon på ammo-objektet:
        terrainTransform.setRotation( new Ammo.btQuaternion( resultQuaternion.x, resultQuaternion.y, resultQuaternion.z, resultQuaternion.w ) );
        terrainMotionState.setWorldTransform(terrainTransform);
    }
    
    toRadians(angle) {
        return Math.PI/180 * angle;
    }

    extractAmmoShapeFromMesh(mesh, scale) {
        function traverseModel(mesh, modelVertices=[]) {
            if (mesh.type === "SkinnedMesh" || mesh.type === "Mesh" || mesh.type === "InstancedMesh") {
                let bufferGeometry = mesh.geometry;
                let attr = bufferGeometry.attributes;
                let position = attr.position;
                let tmpVertices = Array.from(position.array);
                //modelVertices = modelVertices.concat(tmpVertices);
                modelVertices.push(...tmpVertices);
            }
            mesh.children.forEach((child, ndx) => {
                traverseModel(child, modelVertices);
            });
            return modelVertices;
        }

        if (!scale)
            scale = {x:1, y:1, z: 1};

        let vertices = traverseModel(mesh);   // Fungerer kun sammen med BufferGeometry!!
        let ammoMesh = new Ammo.btTriangleMesh();
        for (let i = 0; i < vertices.length; i += 9)
        {
            let v1_x = vertices[i];
            let v1_y = vertices[i+1];
            let v1_z = vertices[i+2];

            let v2_x = vertices[i+3];
            let v2_y = vertices[i+4];
            let v2_z = vertices[i+5];

            let v3_x = vertices[i+6];
            let v3_y = vertices[i+7];
            let v3_z = vertices[i+8];

            let bv1 = new Ammo.btVector3(v1_x, v1_y, v1_z);
            let bv2 = new Ammo.btVector3(v2_x, v2_y, v2_z);
            let bv3 = new Ammo.btVector3(v3_x, v3_y, v3_z);

            ammoMesh.addTriangle(bv1, bv2, bv3);
        }
        let triangleShape = new Ammo.btBvhTriangleMeshShape(ammoMesh, false);
        triangleShape.setMargin( 0.01 );
        triangleShape.setLocalScaling(new Ammo.btVector3(scale.x, scale.y, scale.z));
        return triangleShape;

    }

    //Size: {width, height, depth}
    //Position: {x, y, z},
    //Material
    //Rotation: {x:0, y:0, z:0},
    //Segments= {x:1, y:1}
    addBox(size, position, material, rotation={x:0, y:0, z:0},segments={x:1, y:1}) {
        let geometry = new THREE.BoxBufferGeometry(size.width, size.height, size.depth, segments.x, segments.y);
        let mesh = new THREE.Mesh(geometry, material);
        mesh.position.set(position.x, position.y, position.z)
        mesh.scale.set(1, 1, 1,);
        mesh.rotation.x = rotation.x;
        mesh.rotation.y = rotation.y;
        mesh.rotation.z = rotation.z;
        mesh.receiveShadow = true;
        this.groupMesh.add(mesh);

        let shape = new Ammo.btBoxShape(new Ammo.btVector3(size.width/2, size.height/2, size.depth/2));
        let shapeTrans = new Ammo.btTransform();
        shapeTrans.setIdentity();
        shapeTrans.setOrigin(new Ammo.btVector3(mesh.position.x,mesh.position.y,mesh.position.z));
        let shapeQuat = mesh.quaternion;
        shapeTrans.setRotation( new Ammo.btQuaternion(shapeQuat.x, shapeQuat.y, shapeQuat.z, shapeQuat.w) );
        this.compoundShape.addChildShape(shapeTrans, shape);
    }

    addGameBoardPlane(){
        function addHole(x=0, z=0, r=1){
            let hole = new THREE.Path();
            hole.absarc(x,z,r,0,2*Math.PI, true);
            boardShape.holes.push(hole);
        }

        // Lage Three BoxBufferGeometry (ev. Shape & ExtrudeBufferGeometry):
        let tBoard = new THREE.TextureLoader().load('./res/wood_board_texture_512_512.jpg')
        tBoard.offset.x = 0.5;
        tBoard.offset.y = 0.5;
        tBoard.repeat.x = 0.01;
        tBoard.repeat.y = 0.01;
        let boardMaterial = new THREE.MeshPhongMaterial( { /*color: 0xC709C7,*/ side: THREE.DoubleSide, map: tBoard} );

        const extrudeSettings ={
            steps: 1,
            depth: this.size.height,
            bevelEnabled: false
        };

        let boardShape = new THREE.Shape();
        boardShape.moveTo(-this.size.width/2, -this.size.depth/2);
        boardShape.lineTo(-this.size.width/2, this.size.depth/2);
        boardShape.lineTo(this.size.width/2, this.size.depth/2);
        boardShape.lineTo(this.size.width/2, -this.size.depth/2);
        boardShape.lineTo(-this.size.width/2, -this.size.depth/2);

        //Add more holes!
        addHole(-42,-44,3.4);
        addHole(-20,-44,3);
        addHole(-15,-34,3);
        addHole(-42,-22,3);
        addHole(-20,-16,3);
        addHole(2,-23,4);
        addHole(-44,-3,3);
        addHole(44,-5,3);
        addHole(44,-44,3);
        addHole(26,0,3);
        addHole(-5,7,3);
        addHole(-15,14,3);
        addHole(-27,24,4);
        addHole(-45,44.5,3);
        addHole(-44,35,3);
        addHole(-12,45,3);
        addHole(20,42,3);
        addHole(20,14,3);

        let boardGeometry = new THREE.ExtrudeBufferGeometry(boardShape, extrudeSettings);
        boardGeometry.rotateX(-Math.PI / 2);
        let boardMesh = new THREE.Mesh( boardGeometry, boardMaterial );
        boardMesh.scale.set(1,1,1,);
        boardMesh.receiveShadow = true;
        this.groupMesh.add( boardMesh );

        let ammoTerrainShape = this.extractAmmoShapeFromMesh(boardMesh)
        let shapeTrans = new Ammo.btTransform();
        shapeTrans.setIdentity();
        shapeTrans.setOrigin(new Ammo.btVector3(boardMesh.position.x,boardMesh.position.y,boardMesh.position.z));
        let terrainQuat = boardMesh.quaternion;
        shapeTrans.setRotation( new Ammo.btQuaternion(terrainQuat.x, terrainQuat.y, terrainQuat.z, terrainQuat.w) );
        this.compoundShape.addChildShape(shapeTrans, ammoTerrainShape);
    }
}