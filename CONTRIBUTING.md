
# Contributing to MiniGallery

Thank you for your interest in contributing to MiniGallery! I appreciate your help in making this project better.

## How to Contribute

1.  **Fork the Repository:**
    * Click the "Fork" button at the top right of the repository page.
    * This will create a copy of the repository in your GitHub account.

2.  **Clone Your Fork:**
    * Open your terminal or command prompt.
    * Navigate to the directory where you want to store the project.
    * Run the following command, replacing `your-username` with your GitHub username:
        ```bash
        git clone https://github.com/your-username/MiniGallery.git
        ```

3.  **Create a Branch:**
    * Open the project in your Android Sudio and open terminal
    * Create a new branch for your changes:
        ```bash
        git checkout -b feature/your-feature-name
        ```
      or
        ```bash
        git checkout -b fix/your-bug-fix-name
        ```
      (Replace `your-feature-name` or `your-bug-fix-name` with a descriptive name for your branch.)

4.  **Make Your Changes:**
    * Implement your feature or bug fix.
    * Follow the project's coding style and guidelines (see below).
    * Write appropriate unit tests for your changes.

5.  **Commit Your Changes:**
    * Add your changes to the staging area:
        ```bash
        git add .
        ```
    * Commit your changes with a #issue-number plus descriptive message:
        ```bash
        git commit -m "#32243 Add feature your-feature-description"
        ```
      or
        ```bash
        git commit -m "#45454 Fix bug your-bug-fix-description"
        ```
    **Avoid Conventional Commits**  MiniGallery currently does not use the Conventional Commits format.

6.  **Push Your Changes:**
    * Push your branch to your forked repository:
        ```bash
        git push origin feature/your-feature-name
        ```
      or
        ```bash
        git push origin fix/your-bug-fix-name
        ```

7.  **Create a Pull Request:**
    * Go to your forked repository on GitHub.
    * Click the "Compare & pull request" button.
    * Please create your PR against the **develop** branch.
    * Write a clear and concise description of your changes.
    * Submit the pull request.

## Coding Style and Guidelines

* Follow the project's existing coding style.
* Use [Ktlint](https://ktlint.github.io/) and [Detekt](https://detekt.dev/) for code quality checks. Run `./gradlew ktlintCheck`, `./gradlew ktlintFormat`, and `./gradlew detekt`.
* Write clear and concise code comments.
* Write unit tests to cover your changes.
* Adhere to Clean Architecture, MVI, and modularization principles.


To automate **Ktlint** checks and formatting before each commit, you can use these Gradle tasks:

`./gradlew addKtlintCheckGitPreCommitHook`: This command adds a Git pre-commit hook that automatically runs Ktlint to check the style of your staged files. The commit will be blocked if any style violations are found.

`./gradlew addKtlintFormatGitPreCommitHook` (Recommended): This command adds a Git pre-commit hook that automatically formats your staged files using Ktlint. Any necessary style fixes are applied, and the corrected files are added back to your commit, ensuring consistent code formatting.

## Reporting Bugs

* If you find a bug, please create a new issue in the issue tracker.
* Provide a clear description of the bug, including steps to reproduce it.
* Include any relevant logs or screenshots.

## Suggesting Enhancements

* If you have an idea for a new feature or enhancement, please create a new issue in the issue tracker.
* Provide a clear description of your idea and explain why it would be beneficial.

## Getting Help

* If you have any questions, please create a new issue or start a discussion.

## Thank You!

Thank you for contributing to MiniGallery! Your contributions are greatly appreciated.