# Deep Dream
My own implementation of the Google Deep Dream code, with added Java Swing GUI

Requirements:
* Java 1.7
* Python 2.7 with scientific libraries (numpy, scipy, PIL, IPython)
* Caffe deep learning framework with its own natural requirements 
* Protobuf library
* A GPU with CUDA support would be great but not necessary

To do:
* Add warning for large images not set to downscale
* Write help text
* Teach the GUI to identify a failure in the Python code
* Implement iterations choice (change # of octaves?)
* Think about: another slider to choose octaves
* Think about: ditch sliders in favour of combo box / spinner
* For some of the modifications above, some additional "Don't touch this unless you know what you are doing" warnings might be a good idea
