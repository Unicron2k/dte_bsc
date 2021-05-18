import * as THREE from "../../lib/three/build/three.module.js";
export class SpringAnchor {
    constructor(physicsWorld, scene) {
        this.scene = scene;
        this.physicsWorld = physicsWorld;

        this.rigidBody = undefined;
        this.mesh = undefined;
    }

    create(pos, color, mass, springTarget, opaque=true){
        // Inputkontroll:
        if (!pos) pos = {x:0, y:0, z:0};
        if (!mass) mass = 10;
        if (!color) color = '#FFFFFF';
        let springScale = {x: 0, y: 2, z: 2};

        //boxMesh
        let springAnchorMesh = new THREE.Mesh(new THREE.BoxBufferGeometry(), new THREE.MeshPhongMaterial({color: "#b31414"}));
        springAnchorMesh.position.set(pos.x, pos.y, pos.z);
        springAnchorMesh.scale.set(springScale.x, springScale.y, springScale.z);
        this.scene.add(springAnchorMesh);

        // boxRB
        let boxShape = new Ammo.btBoxShape(new Ammo.btVector3(springScale.x * 0.5, springScale.y * 0.5, springScale.z * 0.5));
        let springAnchorTransform = new Ammo.btTransform();
        springAnchorTransform.setIdentity();
        springAnchorTransform.setOrigin(new Ammo.btVector3(pos.x, pos.y, pos.z));
        let springMotionState = new Ammo.btDefaultMotionState(springAnchorTransform);
        let localSpringInertia = new Ammo.btVector3(0, 0, 0);
        boxShape.calculateLocalInertia(mass, localSpringInertia);
        let rbInfo = new Ammo.btRigidBodyConstructionInfo(mass, springMotionState, boxShape, localSpringInertia);
        let rbSpringAnchor = new Ammo.btRigidBody(rbInfo);
        rbSpringAnchor.setRestitution(0.0);
        rbSpringAnchor.setFriction(0.0);


        springAnchorMesh.userData.physicsBody = rbSpringAnchor;
        this.physicsWorld.rigidBodies.push(springAnchorMesh);

        //https://stackoverflow.com/questions/46671809/how-to-make-a-spring-constraint-with-buwllet-physics
        let transform1 = new Ammo.btTransform();
        transform1.setIdentity();
        transform1.setOrigin(new Ammo.btVector3(0, 0, 0));
        let transform2 = new Ammo.btTransform();
        transform2.setIdentity();
        transform2.setOrigin(new Ammo.btVector3(0, 0, 0));

        let springConstraint = new Ammo.btGeneric6DofSpringConstraint(
            springTarget,
            rbSpringAnchor,
            transform1,
            transform2,
            true);

        // Removing any restrictions on the y-coordinate of the hanging box
        // by setting the lower limit above the upper one.
        springConstraint.setLinearLowerLimit(new Ammo.btVector3(0.0, 0.0, -10.0));
        springConstraint.setLinearUpperLimit(new Ammo.btVector3(0.0, 0.0, 10.0));

        // NB! Disse er viktig for at ikke den hengende kuben ikke skal rotere om alle akser!!
        // Disse gjør at den hengende boksen ikke roterer når den er festet til en constraint (se side 130 i Bullet-boka).
        springConstraint.setAngularLowerLimit(new Ammo.btVector3(0, 0.0, 0.0));
        springConstraint.setAngularUpperLimit(new Ammo.btVector3(0, 0.0, 0.0));

        springConstraint.enableSpring(2, true);    // Translation on y-axis

        let doorStiffness = 10;
        let doorDamping = 0.9;
        springConstraint.setStiffness(2, doorStiffness);
        springConstraint.setDamping(2, doorDamping);

        this.physicsWorld.addConstraint(springConstraint, false);
    }
}
