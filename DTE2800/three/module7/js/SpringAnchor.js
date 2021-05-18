import * as THREE from "../../lib/three/build/three.module.js";
export class SpringAnchor {
    constructor(physicsWorld, scene) {
        this.scene = scene;
        this.physicsWorld = physicsWorld;

        this.rigidBody = undefined;
        this.mesh = undefined;
    }

    create(pos, mass, springTarget, size, opaque= true){
        // Inputkontroll:
        if (!pos) pos = {x:0, y:0, z:0};
        if (!mass) mass = 10;
        if (!size) size = {x: 0, y: 2, z:2};
        let color = '#FFFFFF';


        // AMMO:
        let boxShape = new Ammo.btBoxShape( new Ammo.btVector3( size.x/2, size.y/2, size.z/2) );  //NB! Delt på 2!!
        let transform = new Ammo.btTransform();
        transform.setIdentity();
        transform.setOrigin( new Ammo.btVector3( pos.x, pos.y, pos.z ) );
        let motionState = new Ammo.btDefaultMotionState( transform );
        let localInertia = new Ammo.btVector3( 0, 0, 0 );
        boxShape.calculateLocalInertia(mass, localInertia);
        this.rigidBody = new Ammo.btRigidBody(new Ammo.btRigidBodyConstructionInfo(mass, motionState, boxShape, localInertia));
        this.rigidBody.setRestitution(0.0);
        this.rigidBody.setFriction(0.0);

        if(springTarget) {
            let constraint1 = new Ammo.btTransform();
            constraint1.setIdentity();
            constraint1.setOrigin(new Ammo.btVector3(0, 0, 0));
            let constraint2 = new Ammo.btTransform()
            constraint2.setIdentity()
            constraint2.setOrigin(new Ammo.btVector3(0 , 0,0));
            let springConstraint = new Ammo.btGeneric6DofSpringConstraint(springTarget, this.rigidBody, constraint1, constraint2, true);

            springConstraint.setLinearLowerLimit(new Ammo.btVector3(0.0, 0.0, -10.0));
            springConstraint.setLinearUpperLimit(new Ammo.btVector3(0.0, 0.0, 10.0));
            springConstraint.setAngularLowerLimit(new Ammo.btVector3(0, 0.0, 0.0));
            springConstraint.setAngularUpperLimit(new Ammo.btVector3(0, 0.0, 0.0));
            springConstraint.enableSpring(2, true);

            let springStiffnes = 10;
            let springDamping = 0.9;
            springConstraint.setStiffness(2, springStiffnes);
            springConstraint.setDamping(2, springDamping);
            this.physicsWorld.addConstraint(springConstraint, false);
        }
        //this.physicsWorld.addRB(this.rigidBody);

        // THREE:
        this.mesh = new THREE.Mesh(
            new THREE.BoxBufferGeometry(),
            new THREE.MeshPhongMaterial({color: color, visible: opaque}));
        this.mesh.position.set(pos.x, pos.y, pos.z);
        this.mesh.scale.set(size.x, size.y, size.z);

        this.mesh.userData.physicsBody = this.rigidBody;
        this.physicsWorld.rigidBodies.push(this.mesh);
        this.scene.add(this.mesh);
    }
}
