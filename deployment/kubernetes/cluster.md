A Kubernetes cluster consists of two types of resources:

* The Control Plane is responsible for managing the cluster. The Control Plane coordinates all activities in your
  cluster, such as scheduling applications, maintaining applications' desired state, scaling applications, and rolling
  out new updates.
* A node is a VM or a physical computer that serves as a worker machine in a Kubernetes cluster. Each node has a
  Kubelet, which is an agent for managing the node and communicating with the Kubernetes control plane.

![cluster](img/module_01_cluster.svg)

A Kubernetes **Pod** is a group of one or more Containers, tied together for the purposes of administration and networking.
A Kubernetes **Deployment** checks on the health of your Pod and restarts the Pod's Container if it terminates. Deployments
are the recommended way to manage the creation and scaling of Pods.

#### port forwarding

kubectl port-forward deployment/hello-node 8080:8080
